import { Component, DestroyRef, inject, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { LogInRequest } from '../../../services/auth/auth.model';
import { AuthService } from '../../../services/auth/auth.service';
import { RouterLink } from '@angular/router';
import { required } from '../../../components/modal/modal.validators';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  private destroyRef = inject(DestroyRef);
  private authService = inject(AuthService);
  private toastService = inject(ToastrService);
  private logInResponse = this.authService.loginResponse;

  isLoading = signal(false);
  imagePath: string = 'assets/images/login.svg';

  form = new FormGroup({
    email: new FormControl('', {
      validators: [required, Validators.email],
    }),
    password: new FormControl('', {
      validators: [required],
    }),
  });

  get isInvalidEmail() {
    return this.form.controls.email.invalid && this.form.controls.email.dirty;
  }

  get isInvalidPassword() {
    return (
      this.form.controls.password.invalid && this.form.controls.password.dirty
    );
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }

    const loginRequest: LogInRequest = {
      username: this.form.value.email as string,
      password: this.form.value.password as string,
    };

    this.isLoading.set(true);

    const subscription = this.authService
      .logInCurrentUser(loginRequest)
      .subscribe({
        error: (error: Error) => {
          this.isLoading.set(false);
          this.toastService.error(error.message, 'Error');
        },
        complete: () => {
          this.isLoading.set(false);
          this.toastService.success(this.logInResponse()?.message, 'Success');
        },
      });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
