import { Component, OnInit } from '@angular/core';
import { ChartModule } from 'primeng/chart';

@Component({
  selector: 'app-attendance-chart',
  standalone: true,
  imports:[ChartModule],
  templateUrl: './attendance-chart.component.html',
  styleUrls: ['./attendance-chart.component.css']
})
export class AttendanceChartComponent implements OnInit {
  data: any;
  options: any;

  ngOnInit() {
    this.data = {
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
      datasets: [
        {
          label: 'Present',
          data: [85, 82, 88, 87, 90, 92],
          backgroundColor: 'rgba(78, 115, 223, 0.05)',
          borderColor: 'rgba(78, 115, 223, 1)',
          pointBackgroundColor: 'rgba(78, 115, 223, 1)',
          pointBorderColor: '#fff',
          pointHoverBackgroundColor: '#fff',
          pointHoverBorderColor: 'rgba(78, 115, 223, 1)',
          borderWidth: 2,
          tension: 0.3
        },
        {
          label: 'Absent',
          data: [5, 8, 6, 7, 4, 3],
          backgroundColor: 'rgba(231, 74, 59, 0.05)',
          borderColor: 'rgba(231, 74, 59, 1)',
          pointBackgroundColor: 'rgba(231, 74, 59, 1)',
          pointBorderColor: '#fff',
          pointHoverBackgroundColor: '#fff',
          pointHoverBorderColor: 'rgba(231, 74, 59, 1)',
          borderWidth: 2,
          tension: 0.3
        }
      ]
    };

    this.options = {
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'top'
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            callback: function(value: any) {
              return value + '%';
            }
          }
        }
      }
    };
  }
}