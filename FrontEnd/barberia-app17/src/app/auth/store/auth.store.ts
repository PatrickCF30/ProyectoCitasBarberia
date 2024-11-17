import { UserSession } from '../models/user.session';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import {
  setError,
  setLoaded,
  setLoading,
  withCallState,
  withStorageSync,
} from '@angular-architects/ngrx-toolkit';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { AuthCredentials } from '../models/auth-credentials';
import { ActivatedRoute, Router } from '@angular/router';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { delay, pipe, switchMap, tap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { HttpErrorResponse } from '@angular/common/http';
import { Authority } from '../models/authority.interface';
import { ToastService } from '../../shared/services/toast.service';

interface AuthState {
  username: string | undefined;
  email: string | undefined;
  token: string | undefined;
  authorities: Authority[];
  userId: number;
}

const initialAuthState: AuthState = {
  username: undefined,
  userId: 0,
  email: undefined,
  authorities: [],
  token: undefined,
};

export const AuthStore = signalStore(
  { providedIn: 'root' },
  withState(initialAuthState),
  withCallState(),
  withStorageSync({
    key: 'auth',
    select: (state) => ({
      token: state.token,
      userId: state.userId,
      authorities: state.authorities, // Agregar authorities a la persistencia
    }),
  }),
  withMethods(
    (
      state,
      authService = inject(AuthService),
      router = inject(Router),
      activatedRoute = inject(ActivatedRoute),
      toastService = inject(ToastService),
    ) => ({
      login: rxMethod<AuthCredentials>(
        pipe(
          tap(() => patchState(state, setLoading())),
          switchMap((credentials) =>
            authService.login(credentials).pipe(
              tapResponse({
                next: async (userSession) => {
                  patchState(state, { ...userSession });
                  // Verificar si el usuario es admin
                  const isAdmin = userSession.authorities?.some(
                    (auth) => auth.authority === 'Admin',
                  );

                  // Determinar la URL de redirección
                  let redirectUrl = '/';
                  if (isAdmin) {
                    redirectUrl = '/admin/citas'; // O la ruta administrativa que prefieras
                  } else {
                    // Usar returnUrl solo si no es admin
                    redirectUrl =
                      activatedRoute.snapshot.queryParams['returnUrl'] || '/';
                  }

                  await router.navigateByUrl(redirectUrl);
                },
                error: (err) => {
                  if (err instanceof HttpErrorResponse && err.status === 401) {
                    patchState(
                      state,
                      setError('Credenciales de acceso incorrectas'),
                    );
                  } else {
                    patchState(state, setError((err as any).message));
                  }
                  toastService.showToast({
                    severity: 'error',
                    summary: 'Error',
                    detail: state.error() ?? 'Algo salió mal',
                  });
                },
                finalize: () => patchState(state, setLoaded()),
              }),
            ),
          ),
        ),
      ),
      logout: async () => {
        patchState(state, initialAuthState);
        await router.navigateByUrl('/auth/login');
      },
    }),
  ),
);
