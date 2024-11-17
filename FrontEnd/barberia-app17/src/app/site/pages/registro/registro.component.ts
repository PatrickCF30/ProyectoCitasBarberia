import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../../../services/usuario.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

interface RegistroUsuario {
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  email: string;
  phone: string;
}

@Component({
  selector: 'app-registrar',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css'],
  standalone: true,
  imports: [FormsModule, RouterModule, CommonModule]
})
export class RegistrarComponent {
  registro: RegistroUsuario = {
    firstName: '',
    lastName: '',
    username: '',
    password: '',
    email: '',
    phone: ''
  };
  registrando = false;

  constructor(private usuarioService: UsuarioService, private router: Router) {}

  registrarUsuario(): void {
    this.registrando = true;
    const datosRegistro = {
      first_name: this.registro.firstName,
      last_name: this.registro.lastName,
      username: this.registro.username,
      password: this.registro.password,
      email: this.registro.email,
      phone: this.registro.phone
    };

    this.usuarioService.registrarPerfilUsuario(datosRegistro).subscribe({
      next: (response: any) => {
        alert('Registro exitoso');
        this.router.navigate(['/auth/login']); // Cambiar por la ruta de inicio de sesión
      },
      error: (error) => {
        console.error('Error en el registro:', error);
        alert(error.error.mensaje || 'Error al registrar el usuario');
      },
      complete: () => {
        this.registrando = false;
      }
    });
  }
}
