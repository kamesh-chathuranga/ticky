import { Event } from '../event/event.model';
import { User } from '../user/user.model';

export interface Ticket {
  id: number;
  price: number;
  isSold: boolean;
  purchaseTimestamp: Date;
  customer: User;
  event: Event;
}

export interface TicketSaleHistory {
  month: string;
  totalSoldTickets: number;
}
