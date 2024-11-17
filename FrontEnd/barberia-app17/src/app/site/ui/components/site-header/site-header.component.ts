import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';
import { AuthFacade } from '../../../../auth/services/auth-facade.service';
import { ButtonDirective } from 'primeng/button';

@Component({
  selector: 'site-header',
  standalone: true,
  imports: [RouterLink, NgOptimizedImage, RouterLinkActive, ButtonDirective],
  templateUrl: './site-header.component.html',
  styles: ``,
})
export class SiteHeaderComponent {
  private readonly authFacade = inject(AuthFacade);

  public isLoggedIn = this.authFacade.isLoggedIn;

  async logout() {
    await this.authFacade.logout();
  }
}
