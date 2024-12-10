import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { RouterOutlet } from '@angular/router';
import { UserService } from '../../services/user/user.service';
import { TokenService } from '../../services/token/token.service';
import { LoadingSpinnerComponent } from '../loading-spinner/loading-spinner.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    HeaderComponent,
    FooterComponent,
    RouterOutlet,
    LoadingSpinnerComponent,
  ],
  templateUrl: './layout.component.html',
})
export class LayoutComponent implements OnInit {
  private userService = inject(UserService);
  private tokenService = inject(TokenService);
  private destroyRef = inject(DestroyRef);
  private token = this.tokenService.getToken();

  isLoading = signal(false);

  ngOnInit(): void {
    this.isLoading.set(true);

    const subscription = this.userService.getCurrentUser().subscribe({
      error: () => {
        this.isLoading.set(false);
        if (this.token) {
          this.tokenService.clearToken();
        }
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
