import { Component, DestroyRef, inject, signal } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { required } from '../modal/modal.validators';
import { UserUpdateRequest } from '../../services/user/user.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './profile.component.html',
})
export class ProfileComponent {
  private userService = inject(UserService);
  private destroyRef = inject(DestroyRef);
  private toastService = inject(ToastrService);

  user = this.userService.user;
  isEdited = signal(false);
  isLoading = signal(false);

  form = new FormGroup({
    firstName: new FormControl(
      { value: this.user()?.firstName, disabled: true },
      {
        validators: [required],
      }
    ),
    lastName: new FormControl(
      { value: this.user()?.lastName, disabled: true },
      {
        validators: [required],
      }
    ),
    phoneNumber: new FormControl(
      { value: this.user()?.contactNumber, disabled: true },
      {
        validators: [required, Validators.pattern(/^\d{10}$/)],
      }
    ),
  });

  get isInvalidFirstName() {
    return (
      this.form.controls.firstName.invalid && this.form.controls.firstName.dirty
    );
  }

  get isInvalidLastName() {
    return (
      this.form.controls.lastName.invalid && this.form.controls.lastName.dirty
    );
  }

  get isInvalidPhoneNumber() {
    return (
      this.form.controls.phoneNumber.invalid &&
      this.form.controls.phoneNumber.dirty
    );
  }

  onEditClick() {
    this.form.enable();
    this.isEdited.set(true);
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }

    const updateRequest: UserUpdateRequest = {
      firstName: this.form.value.firstName as string,
      lastName: this.form.value.lastName as string,
      contactNumber: this.form.value.phoneNumber as string,
    };

    this.isLoading.set(true);

    const subscription = this.userService
      .updateCurrentUser(updateRequest)
      .subscribe({
        error: () => {
          this.isLoading.set(false);
          this.toastService.error('Failed to update user profile', 'Error');
        },
        complete: () => {
          this.isLoading.set(false);
          this.toastService.success('Profile updated successfully', 'Success');
        },
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
