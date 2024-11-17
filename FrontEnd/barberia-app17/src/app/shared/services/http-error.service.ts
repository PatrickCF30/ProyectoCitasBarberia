import type { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class HttpErrorService {
  public formatError(err: HttpErrorResponse): string {
    return this.httpErrorFormatter(err);
  }

  private httpErrorFormatter(err: HttpErrorResponse): string {
    let errorMessage = '';

    if (err.error instanceof ErrorEvent) {
      // client side error occurred
      errorMessage = `Ocurrió un error inesperado: ${err.error.message}`;
    } else {
      // the backend returned an unsuccessful response code
      errorMessage = `
        Servidor respondió con código ${err.status}\n
        Detalle del error: ${err.statusText}
      `;
    }

    return errorMessage;
  }
}
