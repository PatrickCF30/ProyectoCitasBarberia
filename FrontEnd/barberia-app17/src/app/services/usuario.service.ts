import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private baseUrl = 'http://localhost:8190/api/usuario'; // URL del endpoint backend

  constructor(private http: HttpClient) {}

  getPerfilUsuario(headers: HttpHeaders): Observable<any> {
    return this.http.get(`${this.baseUrl}/perfil`, { headers });
  }

  editarPerfilUsuario(datos: any, headers: HttpHeaders): Observable<any> {
    return this.http.put(`${this.baseUrl}/perfil`, datos, { headers });
  }

  registrarPerfilUsuario(datos: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register?role=Customer`, datos);
  }
  
}
