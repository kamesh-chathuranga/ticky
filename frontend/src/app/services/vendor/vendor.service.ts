import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { TokenService } from '../token/token.service';
import { Event, EventCreationRequest } from '../event/event.model';
import { EventService } from '../event/event.service';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class VendorService {
  private httpClient = inject(HttpClient);
  private tokenService = inject(TokenService);
  private eventService = inject(EventService);
  private router = inject(Router);
  private baseUrl = 'http://localhost:8080/api';
  private token = this.tokenService.getToken();

  vendorEvents = signal<Event[]>([]);

  getEventsByVendorId() {
    return this.httpClient
      .get<Event[]>(`${this.baseUrl}/vendor/events`, {
        headers: { Authorization: `Bearer ${this.token}` },
      })
      .pipe(
        tap({
          next: (response) => {
            this.vendorEvents.set(response);
          },
        }),
        catchError((errorResponse: HttpErrorResponse) =>
          throwError(() => new Error(errorResponse.error.message))
        )
      );
  }

  createEvent(imageFile: File, eventCreationRequest: EventCreationRequest) {
    const formData = new FormData();
    formData.append('event', JSON.stringify(eventCreationRequest));
    formData.append('image', imageFile);

    return this.httpClient
      .post<Event>(`${this.baseUrl}/vendor/new-event`, formData, {
        headers: { Authorization: `Bearer ${this.token}` },
      })
      .pipe(
        tap({
          next: (response) => {
            this.eventService.addEvent(response);
            this.vendorEvents.set([...this.vendorEvents(), response]);
          },
        }),
        catchError((errorResponse: HttpErrorResponse) =>
          throwError(() => new Error(errorResponse.error.message))
        )
      );
  }

  releaseTickets(eventId: string, release: boolean) {
    return this.httpClient
      .get<string>(
        `${this.baseUrl}/vendor/events/${eventId}?release=${release}`,
        {
          headers: { Authorization: `Bearer ${this.token}` },
        }
      )
      .pipe(
        tap({
          next: () => {
            this.router.navigate(['events', eventId, 'ticket-status']);
          },
        }),
        catchError(() => throwError(() => new Error("Couldn't create event")))
      );
  }
}
