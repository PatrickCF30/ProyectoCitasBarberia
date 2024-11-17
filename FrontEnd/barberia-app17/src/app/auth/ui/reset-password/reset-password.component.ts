import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';  // Servicio AuthService para interactuar con el backend
import { Router } from '@angular/router';  // Para redirigir después de restablecer la contraseña
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  resetPasswordForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder, 
    private authService: AuthService, 
    private router: Router
  ) {
    // Inicializar el formulario con los campos de token y nueva contraseña
    this.resetPasswordForm = this.formBuilder.group({
      token: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  submit() {
    if (this.resetPasswordForm.valid) {
        const { token, newPassword } = this.resetPasswordForm.value;

        this.authService.resetPassword({ token, newPassword }).subscribe({
            next: (response) => {
                console.log('Contraseña restablecida exitosamente:', response);
                // Redirigir al usuario o mostrar un mensaje de éxito
                this.router.navigate(['/login']);
            },
            error: (error) => {
                console.error('Error al restablecer la contraseña:', error);
                alert(error.error || 'Ocurrió un error al restablecer la contraseña');
            }
        });
    } else {
        console.log('Formulario no válido');
    }
}

}
