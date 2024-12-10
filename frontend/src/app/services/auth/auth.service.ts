import { DestroyRef, inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { LogInRequest, RegisterRequest, LogInResponse } from './auth.model';
import { Router } from '@angular/router';
import { TokenService } from '../token/token.service';
import { User } from '../user/user.model';
import { UserService } from '../user/user.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private httpClient = inject(HttpClient);
  private destroyRef = inject(DestroyRef);
  private tokenService = inject(TokenService);
  private userService = inject(UserService);
  private router = inject(Router);
  private baseUrl = 'http://localhost:8080/api/auth';

  loginResponse = signal<LogInResponse | null>(null);
  registerRespoonse = signal<User | null>(null);

  logInCurrentUser(loginRequest: LogInRequest) {
    return this.httpClient
      .post<LogInResponse>(`${this.baseUrl}/login`, loginRequest)
      .pipe(
        tap({
          next: (response) => {
            this.tokenService.setToken(response.token);
            this.loginResponse.set(response);
            this.router.navigate(['/']);
          },
        }),
        catchError((errResponse: HttpErrorResponse) =>
          throwError(() => new Error(errResponse.error.message))
        )
      );
  }

  registerCurrentUser(registerRequest: RegisterRequest) {
    return this.httpClient
      .post<User>(`${this.baseUrl}/register`, registerRequest)
      .pipe(
        tap({
          next: (response) => {
            this.registerRespoonse.set(response);
          },
        }),
        catchError((errResponse: HttpErrorResponse) =>
          throwError(() => new Error(errResponse.error.message))
        )
      );
  }

  logoutCurrentUser() {
    return this.httpClient.get<void>(`${this.baseUrl}/logout`).pipe(
      tap({
        next: () => {
          this.tokenService.clearToken();
          this.userService.user.set(null);
          this.router.navigate(['/']);
        },
      }),
      catchError(() => throwError(() => new Error('Failed to logout user')))
    );
  }
}
