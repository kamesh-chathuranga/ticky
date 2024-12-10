import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './auth.component.html',
})
export class AuthComponent {
  private tokenService = inject(TokenService);
  private router = inject(Router);

  token = this.tokenService.getToken();

  navigate() {
    this.router.navigate(['/']);
  }
}
