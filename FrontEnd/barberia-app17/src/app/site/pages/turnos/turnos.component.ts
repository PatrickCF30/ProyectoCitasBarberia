import {
  Component,
  inject,
  OnInit,
  signal,
  WritableSignal,
} from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { CitaData, CitasService } from '../../../admin/services/citas.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { JwtService } from '../../../auth/services/jwt.service';
import { AuthFacade } from '../../../auth/services/auth-facade.service';
import { TagModule } from 'primeng/tag';
import { ToastService } from '../../../shared/services/toast.service';
import { patchState, signalState } from '@ngrx/signals';

interface DecodedToken {
  sub: string;
  exp: number;
  role: string;
  nombre: string;
}

@Component({
  selector: 'app-turnos',
  templateUrl: './turnos.component.html',
  styleUrls: ['./turnos.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, TagModule],
})
export class TurnosComponent implements OnInit {
  private readonly authFacade = inject(AuthFacade);
  private readonly toastService = inject(ToastService);

  citasState = signalState<{ citas: CitaData[] }>({
    citas: [],
  });

  userEmail = '';
  citaId: number | null = null;
  userId = this.authFacade.userId;

  constructor(
    private turnosService: CitasService,
    private jwtService: JwtService,
  ) {}

  async ngOnInit() {
    await this.obtenerInfoUsuario();
  }

  async obtenerInfoUsuario() {
    const storedToken = localStorage.getItem('auth');

    if (storedToken) {
      const parsedToken = JSON.parse(storedToken);
      const token = parsedToken.token;

      try {
        const decodedToken = this.jwtService.decodeToken(token) as DecodedToken;
        this.userEmail = decodedToken.sub;
        await this.cargarCitasPorCustomer();
      } catch (error) {
        console.error('Error al decodificar el token:', error);
      }
    }
  }

  async cargarCitasPorCustomer() {
    const citasBd = await this.turnosService.getCitaByCustomer();
    patchState(this.citasState, { citas: citasBd });
  }

  /*cargarCitas(): void {
    const storedToken = localStorage.getItem('auth');

    if (storedToken) {
      const parsedToken = JSON.parse(storedToken);
      const token = parsedToken.token;
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

      // Primer llamado para obtener todas las citas y extraer el ID
      this.turnosService.getAllCitas(headers).subscribe({
        next: (response) => {
          console.log('Respuesta inicial:', response);
          if (response.citas && response.citas.length > 0) {
            // Guardamos el ID de la primera cita que encontremos
            this.citaId = response.citas[0].id;
            console.log('ID encontrado:', this.citaId);

            // Ahora hacemos el llamado específico con ese ID
            if (this.citaId) {
              this.turnosService.getCitaById(this.citaId, headers).subscribe({
                next: (detailResponse) => {
                  console.log('Respuesta detallada:', detailResponse);
                  if (detailResponse.cita) {
                    // Guardamos la cita en el array
                    this.citas = [detailResponse.cita];
                    console.log('Citas actualizadas:', this.citas);
                  }
                },
                error: (error) => {
                  console.error('Error al obtener detalle de cita:', error);
                },
              });
            }
          }
        },
        error: (error) => {
          console.error('Error al cargar las citas:', error);
        },
      });
    }
  }*/

  async cancelarCita(citaId: number) {
    patchState(this.citasState, (state) => ({
      citas: state.citas.map((cita) =>
        cita.id === citaId ? { ...cita, estado: 'CANCELADA' } : cita,
      ),
    }));

    this.turnosService.cancelarCita(citaId).subscribe({
      next: () => {
        this.toastService.showToast({
          severity: 'success',
          detail: 'Reserva cancelada con éxito',
          summary: 'Éxito',
        });
      },
      error: (error) => {
        console.error('Error en la cancelación:', error);
        alert('Error al cancelar la cita');
      },
    });
  }

  formatearFecha(fecha: string): string {
    return new Date(fecha).toLocaleString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }
}
