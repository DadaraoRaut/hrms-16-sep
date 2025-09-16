import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

import { RouterModule, RouterOutlet } from '@angular/router';

import { ToastModule } from 'primeng/toast';

import { SidebarComponent } from '../sidebar/sidebar.component';

import { NavbarComponent } from '../navbar/navbar.component';

import { StatsCardsComponent } from '../stats-cards/stats-cards.component';

import { AttendanceChartComponent } from '../attendance-chart/attendance-chart.component';

import { DepartmentChartComponent } from '../department-chart/department-chart.component';

import { QuickActionsComponent } from '../quick-actions/quick-actions.component';

import { UpcomingEventsComponent } from '../upcoming-events/upcoming-events.component';

import { PendingApprovalsComponent } from '../pending-approvals/pending-approvals.component';

@Component({

  selector: 'app-dashboard',

  standalone: true,

  imports: [

    CommonModule,

    RouterOutlet,

    RouterModule,

    ToastModule,

    SidebarComponent,

    NavbarComponent,

    StatsCardsComponent,

    AttendanceChartComponent,

    DepartmentChartComponent,

    QuickActionsComponent,

    UpcomingEventsComponent,

    PendingApprovalsComponent

  ],

  templateUrl: './dashboard.component.html',

  styleUrls: ['./dashboard.component.css']

})

export class DashboardComponent {

  viewMode = 'HR';

  department = 'All';

  username = 'admin'; // Static username for now

  constructor() { }

  goBack(): void {

    window.history.back();

  }
  logout() {
    console.log('Logging out...');
    // Example: redirect to login page
    window.location.href = '/login';
  }


}

