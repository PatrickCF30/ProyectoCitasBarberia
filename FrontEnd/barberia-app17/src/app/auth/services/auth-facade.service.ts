import { computed, inject, Injectable, Signal } from '@angular/core';
import { AuthStore } from '../store/auth.store';
import { AuthCredentials } from '../models/auth-credentials';
import { Authority } from '../models/authority.interface';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthFacade {
  private readonly authStore = inject(AuthStore);

  get isLoggedIn(): Signal<boolean> {
    return computed(() => Boolean(this.authStore.token()));
  }

  get loading(): Signal<boolean> {
    return this.authStore.loading;
  }

  get error(): Signal<string | null> {
    return this.authStore.error;
  }

  get token(): Signal<string | undefined> {
    return this.authStore.token;
  }

  get username(): Signal<string | undefined> {
    return this.authStore.username;
  }

  get userId() {
    return this.authStore.userId;
  }

  public login(credentials: AuthCredentials) {
    this.authStore.login(credentials);
  }

  public async logout() {
    await this.authStore.logout();
  }

  get authorities(): Signal<Authority[] | undefined> {
    return this.authStore.authorities;
  }

  public async checkInitialRedirect() {
    const token = this.token();
    const authorities = this.authorities();
    const isAdmin = authorities?.some((auth) => auth.authority === 'Admin');

    if (token && isAdmin) {
      await inject(Router).navigate(['/admin/citas']);
    }
  }
}
