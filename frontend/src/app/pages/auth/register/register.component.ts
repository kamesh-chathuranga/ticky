import { Component, DestroyRef, inject, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RouterLink } from '@angular/router';
import { required } from '../../../components/modal/modal.validators';
import { AuthService } from '../../../services/auth/auth.service';
import {
  LogInRequest,
  RegisterRequest,
} from '../../../services/auth/auth.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  private destroyRef = inject(DestroyRef);
  private authService = inject(AuthService);
  private toastService = inject(ToastrService);

  isLoading = signal(false);
  selectedRole: 'ROLE_CUSTOMER' | 'ROLE_VENDOR' | undefined;
  imagePath: string = 'assets/images/register.png';

  form = new FormGroup({
    firstName: new FormControl('', {
      validators: [required],
    }),
    lastName: new FormControl('', {
      validators: [required],
    }),
    email: new FormControl('', {
      validators: [required, Validators.email],
    }),
    password: new FormControl('', {
      validators: [required],
    }),
    phoneNumber: new FormControl('', {
      validators: [required, Validators.pattern(/^\d{10}$/)],
    }),
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

  get isInvalidEmail() {
    return this.form.controls.email.invalid && this.form.controls.email.dirty;
  }

  get isInvalidPassword() {
    return (
      this.form.controls.password.invalid && this.form.controls.password.dirty
    );
  }

  get isInvalidPhoneNumber() {
    return (
      this.form.controls.phoneNumber.invalid &&
      this.form.controls.phoneNumber.dirty
    );
  }

  onSelectRole(role: 'ROLE_CUSTOMER' | 'ROLE_VENDOR') {
    this.selectedRole = role;
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }

    const registerRequest: RegisterRequest = {
      firstName: this.form.value.firstName as string,
      lastName: this.form.value.lastName as string,
      email: this.form.value.email as string,
      password: this.form.value.password as string,
      contactNumber: this.form.value.phoneNumber as string,
      role: this.selectedRole as string,
    };

    this.isLoading.set(true);

    const subscription = this.authService
      .registerCurrentUser(registerRequest)
      .subscribe({
        error: (error: Error) => {
          this.isLoading.set(false);
          this.toastService.error(error.message, 'Error');
        },
        complete: () => {
          this.isLoading.set(false);

          const loginRequest: LogInRequest = {
            username: registerRequest.email,
            password: registerRequest.password,
          };

          const subscription = this.authService
            .logInCurrentUser(loginRequest)
            .subscribe();

          this.destroyRef.onDestroy(() => {
            subscription.unsubscribe();
          });
        },
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
