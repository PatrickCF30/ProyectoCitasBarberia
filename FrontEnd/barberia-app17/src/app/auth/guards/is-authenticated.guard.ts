import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthFacade } from '../services/auth-facade.service';
import { JwtService } from '../services/jwt.service';

export const isAuthenticatedGuard: CanActivateFn = (route, state) => {
  const authFacade = inject(AuthFacade);
  const jwtService = inject(JwtService);
  const router = inject(Router);

  const token = authFacade.token();
  const authorities = authFacade.authorities();
  const isAdmin = authorities?.some((auth) => auth.authority === 'Admin');

  if (!token || jwtService.isTokenExpired(token)) {
    return router.createUrlTree(['/auth/login']);
  }

  // Si es una ruta admin y el usuario no es admin
  if (state.url.includes('/admin') && !isAdmin) {
    return router.createUrlTree(['/home']);
  }

  // Si es admin y está intentando acceder a rutas no admin, redirigir a admin
  if (isAdmin && !state.url.includes('/admin')) {
    return router.createUrlTree(['/admin/citas']);
  }

  return true;
};
