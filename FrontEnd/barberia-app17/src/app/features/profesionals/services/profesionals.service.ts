import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { catchError, EMPTY, firstValueFrom, tap } from 'rxjs';
import { Profesional } from '../models/profesional.interface';
import { Service } from '../../barber-services/models/service.interface';

@Injectable({
  providedIn: 'root',
})
export class ProfesionalsService {
  private readonly _http = inject(HttpClient);
  private readonly _urlBase = `${environment.apiUrl}/employees`;

  public async getAll() {
    const url = `${this._urlBase}/enable`;

    const response = await firstValueFrom(
      this._http.get<{ empleados: Profesional[] }>(url),
    );

    return response.empleados;
  }

  public getServicesByEmployee(employeeId: number) {
    const url = `${this._urlBase}/${employeeId}/services`;

    return this._http.get<{ employee: string; services: Service[] }>(url).pipe(
      tap((res) => console.log(res)),
      catchError((err) => {
        console.log(err);
        return EMPTY;
      }),
    );
  }
}
