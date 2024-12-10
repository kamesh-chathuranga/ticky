import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { EventCardComponent } from '../../../../components/event-card/event-card.component';
import { ModalComponent } from '../../../../components/modal/modal.component';
import { VendorService } from '../../../../services/vendor/vendor.service';
import { LoadingSpinnerComponent } from "../../../../components/loading-spinner/loading-spinner.component";
import { AlertComponent } from "../../../../components/alert/alert.component";

@Component({
  selector: 'app-my-events',
  standalone: true,
  imports: [EventCardComponent, ModalComponent, LoadingSpinnerComponent, AlertComponent],
  templateUrl: './my-events.component.html',
})
export class MyEventsComponent implements OnInit {
  private vendorService = inject(VendorService);
  private destroyRef = inject(DestroyRef);

  events = this.vendorService.vendorEvents;
  isLoading = signal(false);
  error = signal('');

  ngOnInit(): void {
    this.isLoading.set(true);

    const subscription = this.vendorService.getEventsByVendorId().subscribe({
      error: (error: Error) => {
        this.isLoading.set(false);
        this.error.set(error.message);
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
