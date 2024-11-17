import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class JwtService {
  public isTokenExpired(token: string) {
    try {
      const timeToExpiration = this.getTimeToExpiration(token);
      return timeToExpiration <= 0;
    } catch {
      return true;
    }
  }

  public decodeToken(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      return JSON.parse(decoded);
    } catch {
      return null;
    }
  }

  public getTimeToExpiration(token: string): number {
    try {
      const payload = this.decodeToken(token);
      if (!payload || !payload.exp) {
        return 0;
      }

      const currentTime = Math.floor(Date.now() / 1000);
      return Math.max(0, payload.exp - currentTime);
    } catch {
      return 0;
    }
  }
}
