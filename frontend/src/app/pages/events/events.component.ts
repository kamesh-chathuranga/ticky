import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { EventService } from '../../services/event/event.service';
import { EventCardComponent } from '../../components/event-card/event-card.component';
import { LoadingSpinnerComponent } from "../../components/loading-spinner/loading-spinner.component";
import { AlertComponent } from "../../components/alert/alert.component";

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [EventCardComponent, LoadingSpinnerComponent, AlertComponent],
  templateUrl: './events.component.html',
})
export class EventsComponent implements OnInit {
  private eventService = inject(EventService);
  private destroyRef = inject(DestroyRef);

  events = this.eventService.allEvents;
  isLoading = signal(false);
  error = signal('');

  ngOnInit(): void {
    this.isLoading.set(true);

    const subscription = this.eventService.getAllEvents().subscribe({
      error: (error: Error) => {
        this.isLoading.set(false);
        this.error.set(error.message);
      },
      complete: () => {
        this.isLoading.set(false);
      },
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
