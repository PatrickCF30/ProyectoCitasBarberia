import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { firstValueFrom, Observable } from 'rxjs';
import { ToastService } from '../../../shared/services/toast.service';

@Injectable({
  providedIn: 'root',
})
export class AppointmentService {
  private readonly _http = inject(HttpClient);
  private readonly _urlBase = `${environment.apiUrl}/citas`;

  getAvailableTimes(empleadoId: number, fecha: string): Observable<any> {
    return this._http.get(
      `${this._urlBase}/${empleadoId}/disponibilidad?fecha=${fecha}`,
    );
  }

  reservar(
    empleadoId: number,
    servicioId: number,
    fecha: Date,
    horario: string,
    customerId: number,
  ) {
    const body = { empleadoId, servicioId, fecha, horario, customerId };

    const response = firstValueFrom(
      this._http.post(`${this._urlBase}/reservar`, body),
    );

    return response;
  }
}
