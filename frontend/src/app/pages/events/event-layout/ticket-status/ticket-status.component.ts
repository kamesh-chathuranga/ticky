import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { TokenService } from '../../../../services/token/token.service';
import { Notification } from './notification.model';
import { Router } from '@angular/router';
import { VendorService } from '../../../../services/vendor/vendor.service';

@Component({
  selector: 'app-ticket-status',
  standalone: true,
  imports: [],
  templateUrl: './ticket-status.component.html',
})
export class TicketStatusComponent implements OnInit {
  private vendorService = inject(VendorService);
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);
  private token = inject(TokenService).getToken();
  private notificationSubscription: any;
  private eventId = signal('');
  socketClient: any = null;
  logs = signal<Notification[]>([]);

  ngOnInit(): void {
    const url = this.router.url;
    this.eventId.set(url.split('/')[2]);

    let ws = new SockJS('http://localhost:8080/api/ws');
    this.socketClient = Stomp.over(ws);

    this.socketClient.connect({ Authorization: 'Bearer ' + this.token }, () => {
      this.notificationSubscription = this.socketClient.subscribe(
        `/user/${this.eventId()}/notification`,
        (message: any) => {
          const notification: Notification = JSON.parse(message.body);
          this.logs.set([...this.logs(), notification]);
        }
      );
    });
  }

  onStopReleasing() {
    const subscription = this.vendorService
      .releaseTickets(this.eventId(), false)
      .subscribe({
        error: (error: Error) => {},
        complete: () => {},
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
