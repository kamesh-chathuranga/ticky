import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TokenService {
  setToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string {
    return localStorage.getItem('token') || '';
  }

  clearToken() {
    localStorage.removeItem('token');
  }
}
