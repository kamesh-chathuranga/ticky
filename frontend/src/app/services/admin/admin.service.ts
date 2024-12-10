import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { TokenService } from '../token/token.service';
import { TicketPoolConfig } from './config.model';
import { BaseResponse } from '../base-response.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private httpClient = inject(HttpClient);
  private token = inject(TokenService).getToken();

  configResponse = signal<BaseResponse | null>(null);

  configureTicketPool(config: TicketPoolConfig) {
    return this.httpClient
      .post<BaseResponse>(
        'http://localhost:8080/api/admin/ticket-pool',
        config,
        {
          headers: { Authorization: `Bearer ${this.token}` },
        }
      )
      .pipe(
        tap({
          next: (response) => {
            this.configResponse.set(response);
          },
        }),
        catchError((errResponse: HttpErrorResponse) =>
          throwError(() => new Error(errResponse.error.message))
        )
      );
  }
}
