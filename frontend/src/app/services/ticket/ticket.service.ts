import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Ticket, TicketSaleHistory } from './ticket.model';
import { catchError, tap, throwError } from 'rxjs';
import { TokenService } from '../token/token.service';

@Injectable({ providedIn: 'root' })
export class TicketService {
  private httpClient = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api';

  token = inject(TokenService).getToken();
  historyData = signal<TicketSaleHistory[]>([]);
  userTickets = signal<Ticket[]>([]);

  getSaleHistory(year: number) {
    return this.httpClient
      .get<TicketSaleHistory[]>(`${this.baseUrl}/tickets/sales/${year}`, {
        headers: { Authorization: `Bearer ${this.token}` },
      })
      .pipe(
        tap({
          next: (response) => {
            this.historyData.set(response);
          },
        }),
        catchError((errorResponse: HttpErrorResponse) =>
          throwError(() => new Error(errorResponse.error.message))
        )
      );
  }

  getUserTickets() {
    return this.httpClient
      .get<Ticket[]>(`${this.baseUrl}/user/tickets`, {
        headers: { Authorization: `Bearer ${this.token}` },
      })
      .pipe(
        tap({
          next: (response) => {
            this.userTickets.set(response);
          },
        }),
        catchError((errorResponse: HttpErrorResponse) =>
          throwError(() => new Error(errorResponse.error.message))
        )
      );
  }
}
