import { RouterModule, Routes } from '@angular/router';
import { SiteLayoutComponent } from './ui/layouts/site-layout/site-layout.component';
import { HomeComponent } from './pages/home/home.component';
import { ProfesionalsComponent } from './pages/profesionals/profesionals.component';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { ProfesionalDetailComponent } from './ui/components/profesional-detail/profesional-detail.component';
import { ServicesByProfesionalComponent } from './pages/services-by-profesional/services-by-profesional.component';
import { isAuthenticatedGuard } from '../auth/guards/is-authenticated.guard';
import { ForgotPasswordComponent } from '../auth/ui/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from '../auth/ui/reset-password/reset-password.component';
import { BookAppointmentComponent } from './pages/book-appointment/book-appointment.component';
import { RegistrarComponent } from './pages/registro/registro.component';
import { BarberServicesService } from '../features/barber-services/services/barber-services.service';
import { TurnosComponent } from './pages/turnos/turnos.component';

export const SITE_ROUTES: Routes = [
  {
    path: '',
    component: SiteLayoutComponent,
    children: [
      {
        path: 'home',
        component: HomeComponent,
      },
      {
        path: 'profesionals',
        component: ProfesionalsComponent,
      },
      {
        path: 'perfil',
        component: PerfilComponent,
        canActivate: [isAuthenticatedGuard],
      },
      {
        path: 'turnos',
        component: TurnosComponent,
        canActivate: [isAuthenticatedGuard],
      },
      {
        path: 'profesionals/:id',
        component: ProfesionalDetailComponent,
        canActivate: [isAuthenticatedGuard],
      },
      {
        path: 'profesionals/:profesionalId/services',
        component: ServicesByProfesionalComponent,
        canActivate: [isAuthenticatedGuard],
      },
      {
        path: 'profesionals/:profesionalId/services/:serviceId/book-appointment',
        component: BookAppointmentComponent,
        canActivate: [isAuthenticatedGuard],
        resolve: {
          service: BarberServicesService,
        },
      },
      {
        path: 'auth/forgot-password',
        component: ForgotPasswordComponent,
      },
      {
        path: 'auth/reset-password',
        component: ResetPasswordComponent,
      },
      {
        path: 'auth/registro',
        component: RegistrarComponent,
      },
      {
        path: '**',
        redirectTo: 'home',
      },
    ],
  },
];
