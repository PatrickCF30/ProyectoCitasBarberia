import { Component, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';
import { AuthFacade } from '../../../auth/services/auth-facade.service';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './admin-layout.component.html'
})
export class AdminLayoutComponent {
  private readonly authFacade = inject(AuthFacade);
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  async logout() {
    await this.authFacade.logout();
  }
}