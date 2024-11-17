import { Component, inject } from '@angular/core';
import { LoginFormComponent } from '../../ui/login-form/login-form.component';
import { AuthCredentials } from '../../models/auth-credentials';
import { ToastService } from '../../../shared/services/toast.service';
import { AuthFacade } from '../../services/auth-facade.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [LoginFormComponent],
  templateUrl: './login-page.component.html',
})
export class LoginPageComponent {
  private readonly toastService = inject(ToastService);
  private readonly authFacade = inject(AuthFacade);

  public isLoading = this.authFacade.loading;
  public error = this.authFacade.error;

  public login({ email, password }: AuthCredentials) {
    this.authFacade.login({ email, password });
  }
}
