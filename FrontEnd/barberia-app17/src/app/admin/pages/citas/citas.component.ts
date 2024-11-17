import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CitasService } from '../../services/citas.service';
import { Cita, CitasResponse } from '../../interfaces/citas.interfaces';
import { Router } from '@angular/router';

@Component({
  selector: 'app-citas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './citas.component.html'
})
export class CitasComponent implements OnInit {
  citas: Cita[] = [];
  loading = false;
  error = false;
  errorMessage = '';

  constructor(private citasService: CitasService, private router: Router) {}

  ngOnInit() {
    this.loadCitas();
  }

  loadCitas() {
    this.loading = true;
    this.error = false;

    this.citasService.getCitas().subscribe({
      next: (response: CitasResponse) => {
        console.log('Respuesta del servidor:', response);
        this.citas = response.citas; // Aquí está el cambio principal
        this.loading = false;
      },
      error: (error: Error) => {
        console.error('Error al cargar citas:', error);
        this.error = true;
        this.errorMessage = 'Error al cargar las citas';
        this.loading = false;
      }
    });
  }

  deleteCita(id: number): void {
    if (confirm('¿Está seguro de que desea eliminar esta cita?')) {
      this.citasService.deleteCita(id).subscribe({
        next: () => {
          this.loadCitas();
        },
        error: (error: Error) => {
          console.error('Error eliminando cita:', error);
          this.errorMessage = 'Error al eliminar la cita';
          this.error = true;
        }
      });
    }
  }

  cerrarSesion(): void {
    localStorage.removeItem('auth');
    this.router.navigate(['/auth/login']);
  }
}