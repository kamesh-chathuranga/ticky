import { Component, input } from '@angular/core';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [],
  templateUrl: './alert.component.html',
})
export class AlertComponent {
  message = input.required<string>();
}
