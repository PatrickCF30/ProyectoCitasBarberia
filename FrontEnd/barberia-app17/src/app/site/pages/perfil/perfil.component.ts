import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpHeaders } from '@angular/common/http';
import { UsuarioService } from '../../../services/usuario.service';
import { Router, RouterModule } from '@angular/router';

interface PerfilUsuario {
  email: string;
  password: string;
  username: string;
  firstName: string;
  lastName: string;
  phone: string;
}

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css'],
  standalone: true,
  imports: [FormsModule, RouterModule]
})
export class PerfilComponent implements OnInit {
  perfil: PerfilUsuario = {
    email: '',
    password: '',
    username: '',
    firstName: '',
    lastName: '',
    phone: ''
  };
  guardando = false;

  constructor(private usuarioService: UsuarioService, private router: Router) {}

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    const storedToken = localStorage.getItem('auth');
    
    if (storedToken) {
      const parsedToken = JSON.parse(storedToken);
      const token = parsedToken.token;
      
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      
      this.usuarioService.getPerfilUsuario(headers).subscribe({
        next: (response: any) => {
          this.perfil = {
            email: response.customer.email,
            password: response.customer.password,
            username: response.customer.username,
            firstName: response.customer.first_name,
            lastName: response.customer.last_name,
            phone: response.customer.phone
          };
          console.log('Perfil cargado:', this.perfil);
        },
        error: (error) => {
          console.error('Error al obtener perfil del usuario:', error);
        }
      });
    }
  }

  guardarCambios(): void {
    this.guardando = true;
    const storedToken = localStorage.getItem('auth');
    
    if (storedToken) {
      const parsedToken = JSON.parse(storedToken);
      const token = parsedToken.token;
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

      // Verificar los valores actuales antes de enviar
      console.log('Valores actuales del perfil:', this.perfil);

      const datosActualizados = {
        email: this.perfil.email,
        password: this.perfil.password,
        username: this.perfil.username,
        first_name: this.perfil.firstName,
        last_name: this.perfil.lastName,
        phone: this.perfil.phone
      };

      console.log('Datos a enviar:', datosActualizados);

      this.usuarioService.editarPerfilUsuario(datosActualizados, headers).subscribe({
        next: (response: any) => {
          console.log('Respuesta del servidor:', response);
          alert('Perfil actualizado exitosamente');
          this.cargarPerfil(); // Recargar los datos
        },
        error: (error) => {
          console.error('Error completo:', error);
          alert(error.error.mensaje || 'Error al actualizar el perfil');
        },
        complete: () => {
          this.guardando = false;
        }
      });
    }
  }

  cerrarSesion(): void {
    // Eliminar el token del localStorage
    localStorage.removeItem('auth');
    
    // Redirigir al usuario a la página de inicio de sesión
    this.router.navigate(['/auth/login']); // Cambia '/login' por la ruta de tu componente de inicio de sesión
  }
}