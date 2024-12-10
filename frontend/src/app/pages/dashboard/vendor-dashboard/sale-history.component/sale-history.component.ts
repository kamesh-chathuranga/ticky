import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { TicketService } from '../../../../services/ticket/ticket.service';
import { Chart, registerables } from 'chart.js';
import { AlertComponent } from '../../../../components/alert/alert.component';
import { LoadingSpinnerComponent } from '../../../../components/loading-spinner/loading-spinner.component';
import { UserService } from '../../../../services/user/user.service';
import { getYearsBetweenDates } from '../../../../util/util';

Chart.register(...registerables);

@Component({
  selector: 'app-sale-history',
  standalone: true,
  imports: [AlertComponent, LoadingSpinnerComponent],
  templateUrl: './sale-history.component.html',
})
export class SaleHistoryComponent implements OnInit {
  private ticketService = inject(TicketService);
  private destroyRef = inject(DestroyRef);
  private userService = inject(UserService);

  history = this.ticketService.historyData;
  user = this.userService.user;
  isLoading = signal(false);
  error = signal('');
  yearList: number[] = getYearsBetweenDates(this.user()?.createdAt!);
  selectedYear = signal(new Date().getFullYear());

  labels: string[] = [];
  values: number[] = [];

  ngOnInit(): void {
    this.getSaleHistory(new Date().getFullYear());
  }

  onSelectYear(year: number) {
    this.selectedYear.set(year);
    this.labels = [];
    this.values = [];
    this.getSaleHistory(year);
  }

  getSaleHistory(year: number): void {
    this.isLoading.set(true);

    const subscription = this.ticketService.getSaleHistory(year).subscribe({
      error: (error: Error) => {
        this.isLoading.set(false);
        this.error.set(error.message);
      },
      complete: () => {
        this.isLoading.set(false);
        this.error.set('');

        if (this.history().length > 0) {
          this.history().map((item) => {
            this.labels.push(item.month);
            this.values.push(item.totalSoldTickets);
          });

          this.displayChart(this.labels, this.values);
        }
      },
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  displayChart(labels: string[], values: number[]): void {
    const chart = new Chart('saleHistoryChart', {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Monthly Ticket Sales',
            data: values,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          y: {
            title: {
              display: true,
              text: 'Number of Tickets Sold',
            },
            ticks: {
              callback: function (value) {
                return Number.isInteger(value) ? value : '';
              },
              stepSize: 1,
            },
            beginAtZero: true,
          },
          x: {
            title: {
              display: true,
              text: 'Month',
            },
          },
        },
      },
    });
  }
}
