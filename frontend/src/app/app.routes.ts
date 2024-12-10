import { Routes } from '@angular/router';
import { LayoutComponent } from './components/layout/layout.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { RegisterComponent } from './pages/auth/register/register.component';
import { HomeComponent } from './pages/home/home.component';
import { EventsComponent } from './pages/events/events.component';
import { AuthComponent } from './pages/auth/auth.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { MyEventsComponent } from './pages/dashboard/vendor-dashboard/my-events.component/my-events.component';
import { SaleHistoryComponent } from './pages/dashboard/vendor-dashboard/sale-history.component/sale-history.component';
import { EventLayoutComponent } from './pages/events/event-layout/event-layout.component';
import { TicketStatusComponent } from './pages/events/event-layout/ticket-status/ticket-status.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EventDetailsComponent } from './pages/events/event-layout/event-details/event-details.component';
import { AboutComponent } from './pages/about/about.component';
import { MyTicketsComponent } from './components/my-tickets/my-tickets.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        component: HomeComponent,
      },
      {
        path: 'events',
        component: EventsComponent,
      },
      {
        path: 'events/:eventId',
        component: EventLayoutComponent,
        children: [
          {
            path: '',
            component: EventDetailsComponent,
          },
          {
            path: 'ticket-status',
            component: TicketStatusComponent,
          },
        ],
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        children: [
          {
            path: 'my-events',
            component: MyEventsComponent,
          },
          {
            path: 'sale-history',
            component: SaleHistoryComponent,
          },
        ],
      },
      {
        path: 'profile',
        component: ProfileComponent,
      },
      {
        path: 'about',
        component: AboutComponent,
      },
      {
        path: 'my-tickets',
        component: MyTicketsComponent,
      },
    ],
  },
  {
    path: 'auth',
    component: AuthComponent,
    children: [
      {
        path: 'login',
        component: LoginComponent,
      },
      {
        path: 'register',
        component: RegisterComponent,
      },
    ],
  },
];
