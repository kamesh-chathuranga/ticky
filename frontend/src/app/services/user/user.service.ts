import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { User, UserUpdateRequest } from './user.model';
import { TokenService } from '../token/token.service';
import { BaseResponse } from '../base-response.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private httpClient = inject(HttpClient);
  private tokenService = inject(TokenService);
  private baseUrl = 'http://localhost:8080/api/user';

  user = signal<User | null>(null);

  getCurrentUser() {
    return this.httpClient
      .get<User>(`${this.baseUrl}`, {
        headers: { Authorization: `Bearer ${this.tokenService.getToken()}` },
      })
      .pipe(
        tap({
          next: (response) => {
            this.user.set(response);
          },
        }),
        catchError((errResponse: HttpErrorResponse) =>
          throwError(() => new Error(errResponse.error.message))
        )
      );
  }

  buyTicket(eventId: string) {
    return this.httpClient
      .get<BaseResponse>(`${this.baseUrl}/events/${eventId}/buy`, {
        headers: { Authorization: `Bearer ${this.tokenService.getToken()}` },
      })
      .pipe(
        tap({
          next: (response) => {
            console.log(response);
          },
        }),
        catchError(() =>
          throwError(() => new Error("Couldn't buy ticket for the event"))
        )
      );
  }

  updateCurrentUser(updateRequest: UserUpdateRequest) {
    return this.httpClient
      .put<User>(`${this.baseUrl}/update`, updateRequest, {
        headers: { Authorization: `Bearer ${this.tokenService.getToken()}` },
      })
      .pipe(
        tap({
          next: (response) => {
            this.user.set(response);
          },
        }),
        catchError((errResponse: HttpErrorResponse) =>
          throwError(() => new Error(errResponse.error.message))
        )
      );
  }
}
