import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';  // Importa tu servicio
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  forgotPasswordForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
    this.forgotPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  submit() {
    if (this.forgotPasswordForm.valid) {
      const email = this.forgotPasswordForm.value.email;

      // Llama al servicio para enviar el email
      this.authService.forgotPassword({ email }).subscribe({
        next: (response) => {
          console.log('Solicitud enviada exitosamente:', response);
          // Muestra un mensaje de éxito o redirige al usuario
          this.router.navigate(['/auth/reset-password']);
        },
        error: (error) => {
          console.error('Error al enviar la solicitud:', error);
          // Maneja el error, muestra un mensaje al usuario
        }
      });
    } else {
      console.log('Formulario no válido');
    }
  }
}
