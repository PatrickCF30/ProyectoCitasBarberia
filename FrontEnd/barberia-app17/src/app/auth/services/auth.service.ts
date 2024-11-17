import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AuthCredentials } from '../models/auth-credentials';
import { LoginResponse } from '../models/login-response.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly _http = inject(HttpClient);
  private readonly _urlBase = environment.url;

  public login(credentials: AuthCredentials): Observable<LoginResponse> {
    const url = `${this._urlBase}/login`;
    return this._http.post<LoginResponse>(url, credentials);
  }

  public forgotPassword(data: { email: string }): Observable<any> {
    const url = `${this._urlBase}/api/password/forgot-password`;
    return this._http.post(url, data);
  }


  public resetPassword(data: { token: string; newPassword: string }): Observable<string> {
    const url = `${this._urlBase}/api/password/reset-password`;
    return this._http.post(url, data, { responseType: 'text' });
}
  
}
