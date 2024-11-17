import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthFacade } from '../services/auth-facade.service';
import { Router } from '@angular/router';
import { EMPTY } from 'rxjs';

const WHITE_LIST = ['login', 'api/services', 'api/employees/enable', 'api/usuario/register','/api/password/forgot-password', 'api/password/reset-password'] as const;

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authFacade = inject(AuthFacade);
  const router = inject(Router);

  const isWhitelisted = WHITE_LIST.some((path) => req.url.includes(path));

  if (isWhitelisted) {
    return next(req);
  }

  const token = authFacade.token();
  console.log(token);

  // Aquí se puede comprobar si el usuario está en una ruta administrativa
  const isAdminRoute = req.url.includes('/admin/');

  if (!token) {
    if (isAdminRoute) {
      // Permite que el guardia maneje la redirección si está en una ruta admin
      return next(req);
    } else {
      router.navigateByUrl('/auth/login');
      return EMPTY;
    }
  }

  const authRequest = req.clone({
    setHeaders: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authRequest);
};
