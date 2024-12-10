import {
  Component,
  DestroyRef,
  inject,
  input,
  OnInit,
  signal,
} from '@angular/core';

import { CurrencyPipe, DatePipe } from '@angular/common';
import { EventService } from '../../../../services/event/event.service';
import { VendorService } from '../../../../services/vendor/vendor.service';
import { UserService } from '../../../../services/user/user.service';
import { LoadingSpinnerComponent } from '../../../../components/loading-spinner/loading-spinner.component';
import { AlertComponent } from '../../../../components/alert/alert.component';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { TokenService } from '../../../../services/token/token.service';
import { DialogBoxComponent } from '../../../../components/dialog-box/dialog-box.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-event-details',
  standalone: true,
  imports: [
    DatePipe,
    CurrencyPipe,
    LoadingSpinnerComponent,
    AlertComponent,
    DialogBoxComponent,
  ],
  templateUrl: './event-details.component.html',
})
export class EventDetailsComponent implements OnInit {
  private eventService = inject(EventService);
  private vendorService = inject(VendorService);
  private userService = inject(UserService);
  private destroyRef = inject(DestroyRef);
  private toastService = inject(ToastrService)
  private token = inject(TokenService).getToken();
  private notificationSubscription: any;
  socketClient: any = null;

  event = this.eventService.event;
  user = this.userService.user;
  eventId = input.required<string>();
  isLoading = signal(false);
  error = signal('');
  availableTickets = signal(this.event()?.availableTickets);

  openDialog = this.eventService.openDialog;
  isReleased = this.eventService.isReleased;

  ngOnInit(): void {
    this.getEventDetails();
    this.getAvailableTickets();
  }

  getAvailableTickets() {
    let ws = new SockJS('http://localhost:8080/api/ws');
    this.socketClient = Stomp.over(ws);

    this.socketClient.connect({ Authorization: 'Bearer ' + this.token }, () => {
      this.notificationSubscription = this.socketClient.subscribe(
        `/user/${this.eventId()}/availableTickets`,
        (message: any) => {
          const availableTickets: number = JSON.parse(message.body);
          this.availableTickets.set(availableTickets);
        }
      );
    });
  }

  getEventDetails() {
    this.isLoading.set(true);

    const subscription = this.eventService
      .getEventById(this.eventId())
      .subscribe({
        error: (error: Error) => {
          this.isLoading.set(false);
          this.error.set(error.message);
        },
        complete: () => {
          this.availableTickets.set(this.event()?.availableTickets);
          this.isLoading.set(false);
        },
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  onTicketRelease() {
    if (this.isReleased()) {
      this.openDialog.set(true);
      return;
    }

    const subscription = this.vendorService
      .releaseTickets(this.eventId(), true)
      .subscribe({
        error: (error: Error) => {},
        complete: () => {
          this.isReleased.set(true);
        },
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  onTicketPurchase() {
    const subscription = this.userService.buyTicket(this.eventId()).subscribe({
      error: (error: Error) => {
        console.log(error);
      },
      complete: () => {
        this.toastService.success('Ticket purchased successfully');
      },
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
