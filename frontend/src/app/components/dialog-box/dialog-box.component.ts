import { Component, EventEmitter, inject, Output } from '@angular/core';
import { EventService } from '../../services/event/event.service';

@Component({
  selector: 'app-dialog-box',
  standalone: true,
  imports: [],
  templateUrl: './dialog-box.component.html',
})
export class DialogBoxComponent {
  private eventService = inject(EventService);

  isReleased = this.eventService.isReleased;
  openDialog = this.eventService.openDialog;
  @Output('onTicketRelease') onTicketRelease: EventEmitter<any> =
    new EventEmitter();

  onReleaseHandler() {
    this.isReleased.set(false);
    this.openDialog.set(false);
    this.onTicketRelease.emit();
  }

  onCloseHandler() {
    this.openDialog.set(false);
    this.isReleased.set(true);
  }
}
