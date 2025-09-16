import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-stats-cards',
  standalone: true,
  imports: [CommonModule,ToastModule],
  templateUrl: './stats-cards.component.html',
  styleUrl: './stats-cards.component.css'
})
export class StatsCardsComponent {
stats = [
    {
      title: 'Total Employees',
      value: '247',
      icon: 'fas fa-users',
      color: 'primary'
    },
    {
      title: 'New Joiners (This Month)',
      value: '12',
      icon: 'fas fa-user-plus',
      color: 'success'
    },
    {
      title: 'Pending Approvals',
      value: '18',
      icon: 'fas fa-clipboard-check',
      color: 'info'
    },
    {
      title: 'Attrition Rate',
      value: '8.2%',
      icon: 'fas fa-chart-line',
      color: 'warning'
    }
  ];

}
