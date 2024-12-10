import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { Event } from './event.model';

@Injectable({ providedIn: 'root' })
export class EventService {
  private httpClient = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api';

  event = signal<Event | null>(null);
  allEvents = signal<Event[]>([]);
  isReleased = signal(false);
  openDialog = signal(false);

  getAllEvents() {
    return this.httpClient.get<Event[]>(`${this.baseUrl}/events`).pipe(
      tap({
        next: (response) => {
          this.allEvents.set(response);
        },
      }),
      catchError((errorResponse) =>
        throwError(() => new Error(errorResponse.error.message))
      )
    );
  }

  getEventById(id: string) {
    return this.httpClient.get<Event>(`${this.baseUrl}/events/${id}`).pipe(
      tap({
        next: (response) => {
          this.event.set(response);
        },
      }),
      catchError((errorResponse) =>
        throwError(() => new Error(errorResponse.error.message))
      )
    );
  }

  addEvent(event: Event) {
    this.allEvents.set([...this.allEvents(), event]);
  }
}
