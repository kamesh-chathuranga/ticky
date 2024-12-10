import { Component } from '@angular/core';
import { LatestEventsComponent } from './latest-events/latest-events.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [LatestEventsComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {}
