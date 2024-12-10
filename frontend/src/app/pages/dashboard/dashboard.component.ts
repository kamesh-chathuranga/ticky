import { Component, inject } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { adminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { VendorDashboardComponent } from './vendor-dashboard/vendor-dashboard.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [adminDashboardComponent, VendorDashboardComponent],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  user = inject(UserService).user;
}
