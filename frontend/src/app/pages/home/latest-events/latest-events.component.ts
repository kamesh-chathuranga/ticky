import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { EventCardComponent } from '../../../components/event-card/event-card.component';
import { EventService } from '../../../services/event/event.service';
import { AlertComponent } from '../../../components/alert/alert.component';
import { LoadingSpinnerComponent } from "../../../components/loading-spinner/loading-spinner.component";

@Component({
  selector: 'app-latest-events',
  standalone: true,
  imports: [EventCardComponent, AlertComponent, LoadingSpinnerComponent],
  templateUrl: './latest-events.component.html',
})
export class LatestEventsComponent implements OnInit {
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
