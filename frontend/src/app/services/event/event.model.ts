import { User } from '../user/user.model';

export interface Event {
  id: number;
  name: string;
  description: string;
  ticketPrice: number;
  totalTickets: number;
  availableTickets: number;
  imageUrl: string;
  vendor: User;
  location: string;
  eventDate: Date;
  category: EventCategory;
}

export enum EventCategory {
  MUSIC = 'MUSIC',
  SPORTS = 'SPORTS',
  ARTS = 'ARTS',
  THEATER = 'THEATER',
  COMEDY = 'COMEDY',
  EDUCATION = 'EDUCATION',
  CHARITY = 'CHARITY',
  FAMILY = 'FAMILY',
  FESTIVAL = 'FESTIVAL',
  FOOD_DRINKS = 'FOOD_DRINKS',
  PARTY = 'PARTY',
  NETWORKING = 'NETWORKING',
  CONFERENCE = 'CONFERENCE',
  FAIR = 'FAIR',
  TOURNAMENT = 'TOURNAMENT',
  OTHER = 'OTHER',
}

export interface EventCreationRequest {
  name: string;
  description: string;
  ticketPrice: number;
  totalTickets: number;
  location: string;
  eventDate: Date;
  category: EventCategory;
}
