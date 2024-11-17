import { Routes } from '@angular/router';
import { LoginPageComponent } from './auth/pages/login-page/login-page.component';
import { isAuthenticatedGuard } from './auth/guards/is-authenticated.guard';
import { AdminLayoutComponent } from './admin/components/admin-layout/admin-layout.component';

export const routes: Routes = [
  {
    path: 'auth/login',
    component: LoginPageComponent,
  },
  {
    path: 'admin',
    component: AdminLayoutComponent,
    loadChildren: () =>
      import('./admin/admin.module').then((m) => m.AdminModule),
    canActivate: [isAuthenticatedGuard],
  },
  {
    path: 'dashboard',
    loadChildren: () =>
      import('./dashboard/dashboard.routes').then((c) => c.DASHBOARD_ROUTES),
  },
  {
    path: '',
    loadChildren: () => import('./site/site.routes').then((r) => r.SITE_ROUTES),
  },
  {
    path: '**',
    redirectTo: 'home',
  },
];
