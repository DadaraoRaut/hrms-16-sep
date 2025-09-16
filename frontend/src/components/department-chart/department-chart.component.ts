import { Component, OnInit } from '@angular/core';

import { ChartModule } from 'primeng/chart'; // required import
 
@Component({

  selector: 'app-department-chart',

  standalone: true,

  imports: [ChartModule],

  templateUrl: './department-chart.component.html',

  styleUrls: ['./department-chart.component.css']

})

export class DepartmentChartComponent implements OnInit {

  data: any;

  options: any;
 
  ngOnInit() {

    this.data = {

      labels: ['Sales', 'Technology', 'HR', 'Finance', 'Operations'],

      datasets: [{

        data: [35, 40, 10, 10, 5],

        backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc', '#f6c23e', '#e74a3b'],

        hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf', '#dda20a', '#be2617'],

      }]

    };
 
    this.options = {

      maintainAspectRatio: false,

      plugins: {

        legend: {

          display: false

        }

      },

      cutout: '80%'

    };

  }

}

 