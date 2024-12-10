import { Component, input } from '@angular/core';
import { Ticket } from '../../services/ticket/ticket.model';
import { CurrencyPipe, DatePipe } from '@angular/common';

@Component({
  selector: 'app-ticket',
  standalone: true,
  imports: [CurrencyPipe, DatePipe],
  templateUrl: './ticket.component.html',
})
export class TicketComponent {
  ticket = input.required<Ticket>();
}
