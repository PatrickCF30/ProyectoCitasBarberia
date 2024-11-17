import { Component, inject, input, output, signal } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { Ripple } from 'primeng/ripple';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DividerModule } from 'primeng/divider';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { NgClass } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { FormValidatorsService } from '../../../shared/services/form-validator.service';
import { AuthCredentials } from '../../models/auth-credentials';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'login-form',
  standalone: true,
  imports: [
    ButtonDirective,
    Ripple,
    ReactiveFormsModule,
    DividerModule,
    InputGroupModule,
    InputGroupAddonModule,
    NgClass,
    InputTextModule,
    RouterLink,
  ],
  templateUrl: './login-form.component.html',
  styles: ``,
})
export class LoginFormComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly formsValidatorsService = inject(FormValidatorsService);

  public isLoading = input<boolean>(false);

  public submittedForm = output<AuthCredentials>();

  public seePass = signal<boolean>(false);

  // Login Form
  public readonly loginForm = this.formBuilder.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  public showPassword(): void {
    this.seePass.update((currentValue) => !currentValue);
  }

  public isNotValidField(field: string) {
    return this.formsValidatorsService.isNotValidField(this.loginForm, field);
  }

  public submit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();

      return;
    }

    this.submittedForm.emit({
      email: this.loginForm.controls.username.value,
      password: this.loginForm.controls.password.value,
    });
  }
}
