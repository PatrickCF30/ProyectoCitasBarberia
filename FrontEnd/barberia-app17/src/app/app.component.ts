import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastRailComponent } from './shared/components/ui/toast-rail/toast-rail.component';
import { PrimeNGConfig } from 'primeng/api';
import { AuthFacade } from './auth/services/auth-facade.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastRailComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  title = 'barberia-app17';

  constructor(
    private primengConfig: PrimeNGConfig,
    private authFacade: AuthFacade
  ) {}

  ngOnInit(): void {
    // Configuración de PrimeNG
    this.primengConfig.ripple = true;
    
    // Verificar y redirigir si es necesario
    this.authFacade.checkInitialRedirect();
  }
}