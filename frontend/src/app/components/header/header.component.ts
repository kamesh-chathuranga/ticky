import { Component, DestroyRef, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../services/user/user.service';
import { AuthService } from '../../services/auth/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private destroyRef = inject(DestroyRef);
  private toastService = inject(ToastrService);

  currentUser = this.userService.user;
  imagePath: string = 'assets/images/logo.png';

  onLogout() {
    const subscription = this.authService.logoutCurrentUser().subscribe({
      error: (error: Error) => {
        this.toastService.error(error.message, 'Error');
      },
      complete: () => {
        this.toastService.success('Logged out successfully', 'Success');
      },
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
