import { Component, DestroyRef, inject, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { required } from './modal.validators';
import {
  EventCategory,
  EventCreationRequest,
} from '../../services/event/event.model';
import { VendorService } from '../../services/vendor/vendor.service';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './modal.component.html',
})
export class ModalComponent {
  private destroyRef = inject(DestroyRef);
  private vendorService = inject(VendorService);

  isLoading = signal(false);
  error = signal('');
  EventCategory = EventCategory;

  selectedCategory: EventCategory | undefined;
  selectedImageFile: File | null = null;

  form = new FormGroup({
    name: new FormControl('', {
      validators: [required],
    }),
    description: new FormControl('', {
      validators: [required],
    }),
    ticketPrice: new FormControl('', {
      validators: [Validators.min(50)],
    }),
    totalTickets: new FormControl('', {
      validators: [Validators.min(1)],
    }),
    location: new FormControl('', {
      validators: [required],
    }),
    eventDate: new FormControl('', {
      validators: [required],
    }),
  });

  get isInvalidName() {
    return this.form.controls.name.invalid && this.form.controls.name.dirty;
  }

  get isInvalidDescription() {
    return (
      this.form.controls.description.invalid &&
      this.form.controls.description.dirty
    );
  }

  get isInvalidTicketPrice() {
    return (
      this.form.controls.ticketPrice.invalid &&
      this.form.controls.ticketPrice.dirty
    );
  }

  get isInvalidTotalTickets() {
    return (
      this.form.controls.totalTickets.invalid &&
      this.form.controls.totalTickets.dirty
    );
  }

  get isInvalidLocation() {
    return (
      this.form.controls.location.invalid && this.form.controls.location.dirty
    );
  }

  get isInvalidEventDate() {
    return (
      this.form.controls.eventDate.invalid && this.form.controls.eventDate.dirty
    );
  }

  onFileSelected(event: any) {
    this.selectedImageFile = <File>event.target.files[0];
  }

  onSelectCategory(categoty: EventCategory) {
    this.selectedCategory = categoty;
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }

    this.error.set('');
    
    const event: EventCreationRequest = {
      name: this.form.controls.name.value as string,
      description: this.form.controls.description.value as string,
      ticketPrice: Number(this.form.controls.ticketPrice.value),
      totalTickets: Number(this.form.controls.totalTickets.value),
      location: this.form.controls.location.value as string,
      eventDate: new Date(this.form.controls.eventDate.value as string),
      category: this.selectedCategory!,
    };

    this.isLoading.set(true);

    const subscription = this.vendorService
      .createEvent(this.selectedImageFile!, event)
      .subscribe({
        error: (error: Error) => {
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
