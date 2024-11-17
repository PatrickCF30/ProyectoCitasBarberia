import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { firstValueFrom, Observable } from 'rxjs';
import { CitasResponse } from '../interfaces/citas.interfaces';

export interface CitaData {
  id: number;
  fecha: string;
  servicio: string;
  duracion: string;
  cancelled: boolean;
  empleado: string;
  customer: string;
  horario: string;
  estado: string;
}

interface CitaResponse {
  citas: CitaData[];
  mensaje: string;
  status: string;
}

interface CitaDetailResponse {
  mensaje: string;
  cita: CitaData;
  status: string;
}

@Injectable({
  providedIn: 'root',
})
export class CitasService {
  private baseUrl = 'http://localhost:8190/api';

  constructor(private http: HttpClient) {}

  getCitas(): Observable<CitasResponse> {
    return this.http.get<CitasResponse>(`${this.baseUrl}/citas`);
  }

  deleteCita(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/citas/${id}`);
  }

  getAllCitas(headers: HttpHeaders): Observable<CitaResponse> {
    return this.http.get<CitaResponse>(`${this.baseUrl}/citas`, { headers });
  }

  getCitaById(
    id: number,
    headers: HttpHeaders,
  ): Observable<CitaDetailResponse> {
    return this.http.get<CitaDetailResponse>(`${this.baseUrl}/citas/${id}`, {
      headers,
    });
  }

  async getCitaByCustomer() {
    const response = await firstValueFrom(
      this.http.get<CitaResponse>(`${this.baseUrl}/citas/listarPorCustomer`),
    );

    console.log(response.citas);
    return response.citas;
  }

  cancelarCita(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/citas/cancelar/${id}`, {});
  }
}
