import { Component, DestroyRef, inject, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AdminService } from '../../../services/admin/admin.service';
import { TicketPoolConfig } from '../../../services/admin/config.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './admin-dashboard.component.html',
})
export class adminDashboardComponent {
  private destroyRef = inject(DestroyRef);
  private adminService = inject(AdminService);
  private toastService = inject(ToastrService);
  private configResponse = this.adminService.configResponse;

  isLoading = signal(false);

  form = new FormGroup({
    maxTicketCapacity: new FormControl('', {
      validators: [Validators.required, Validators.min(1)],
    }),
    ticketReleaseRate: new FormControl('', {
      validators: [Validators.required, Validators.min(50)],
    }),
    customerRetrievalRate: new FormControl('', {
      validators: [Validators.required, Validators.min(50)],
    }),
  });

  get isInvalidMaxTicketCapacity() {
    return (
      this.form.controls.maxTicketCapacity.invalid &&
      this.form.controls.maxTicketCapacity.dirty
    );
  }

  get isInvalidTicketReleaseRate() {
    return (
      this.form.controls.ticketReleaseRate.invalid &&
      this.form.controls.ticketReleaseRate.dirty
    );
  }

  get isInvalidCustomerRetrievalRate() {
    return (
      this.form.controls.customerRetrievalRate.invalid &&
      this.form.controls.customerRetrievalRate.dirty
    );
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }

    const config: TicketPoolConfig = {
      maxTicketCapacity: Number(this.form.controls.maxTicketCapacity.value),
      ticketReleaseRate: Number(this.form.controls.ticketReleaseRate.value),
      customerRetrievalRate: Number(
        this.form.controls.customerRetrievalRate.value
      ),
    };

    this.isLoading.set(true);

    const subscription = this.adminService
      .configureTicketPool(config)
      .subscribe({
        error: (error: Error) => {
          this.isLoading.set(false);
          this.toastService.error(error.message, 'Error');
        },
        complete: () => {
          this.isLoading.set(false);
          this.toastService.success(this.configResponse()?.message, 'Success');
        },
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
