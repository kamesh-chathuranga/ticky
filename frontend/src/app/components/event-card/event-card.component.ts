import {Component, input} from '@angular/core';
import {Event} from '../../services/event/event.model';
import {DatePipe} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-event-card',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './event-card.component.html',
})
export class EventCardComponent {
  event = input.required<Event>();
}
