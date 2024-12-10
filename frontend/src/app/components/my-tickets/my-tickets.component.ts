import {
  Component,
  DestroyRef,
  inject,
  input,
  OnInit,
  signal,
} from '@angular/core';
import { TicketService } from '../../services/ticket/ticket.service';
import { LoadingSpinnerComponent } from '../loading-spinner/loading-spinner.component';
import { AlertComponent } from '../alert/alert.component';
import { TicketComponent } from '../ticket/ticket.component';

@Component({
  selector: 'app-my-tickets',
  standalone: true,
  imports: [LoadingSpinnerComponent, AlertComponent, TicketComponent],
  templateUrl: './my-tickets.component.html',
})
export class MyTicketsComponent implements OnInit {
  private ticketService = inject(TicketService);
  private destroyRef = inject(DestroyRef);

  userTickets = this.ticketService.userTickets;
  isLoading = signal(false);
  error = signal('');

  ngOnInit(): void {
    this.isLoading.set(true);

    const subscription = this.ticketService.getUserTickets().subscribe({
      error: (error: Error) => {
        this.isLoading.set(false);
        this.error.set("Couldn't load your tickets. Please try again later.");
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
