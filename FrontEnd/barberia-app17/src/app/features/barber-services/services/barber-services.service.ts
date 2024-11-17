import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { firstValueFrom } from 'rxjs';
import { Service } from '../models/service.interface';
import {
  ActivatedRouteSnapshot,
  MaybeAsync,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class BarberServicesService implements Resolve<Service> {
  private readonly _http = inject(HttpClient);
  private readonly _urlBase = environment.apiUrl;

  public async getAll() {
    const url = `${this._urlBase}/servicios`;

    const response = await firstValueFrom(
      this._http.get<{ services: Service[] }>(url),
    );

    return response.services;
  }

  public async getById(id: number) {
    const url = `${this._urlBase}/servicios/${id}`;
    const response = await firstValueFrom(
      this._http.get<{ services: Service }>(url),
    );

    return response.services;
  }

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): MaybeAsync<Service> {
    return this.getById(Number(route.paramMap.get('serviceId')));
  }
}
