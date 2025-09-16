import { Component, ViewChild } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';

import { AnnouncementComponent } from '../components/announcement/announcement.component';
import { AppreciationComponent } from '../components/appreciation/appreciation.component';
import { AttendanceComponent } from '../components/attendance/attendance.component';
import { AuditLogComponent } from '../components/audit-log/audit-log.component';
import { BenefitComponent } from '../components/benefit/benefit.component';
import { ComplianceComponent } from '../components/compliance/compliance.component';
import { DEIComponent } from '../components/dei/dei.component';
import { DepartmentComponent } from '../components/department/department.component';
import { DocumentComponent } from '../components/document/document.component';
import { EmployeeBenefitComponent } from '../components/employee-benefit/employee-benefit.component';
import { LeaveComponent } from '../components/leave/leave.component';
import { LeaveTypeComponent } from '../components/leave-type/leave-type.component';

import { PayrollComponent } from '../components/payroll/payroll.component';
import { PerformanceReviewComponent } from '../components/performance-review/performance-review.component';
import { SalaryStructureComponent } from '../components/salary-structure/salary-structure.component';
import { FeedbackComponent } from '../components/Survey/feedback/feedback.component';
import { TicketComponent } from '../components/ticket/ticket.component';
import { TrainingComponent } from '../components/training/training.component';
import { UserComponent } from '../components/user/user.component';
import { HomeComponent } from '../components/home/home.component';
import { LoginComponent } from '../components/login/login.component';
import { DashboardComponent } from '../components/dashboard/dashboard.component';
import { LeaveCalendarComponent } from '../components/leave-calendar/leave-calendar.component';
import { SidebarComponent } from '../components/sidebar/sidebar.component';
import { NavbarComponent } from '../components/navbar/navbar.component';
import { StatsCardsComponent } from '../components/stats-cards/stats-cards.component';
import { AttendanceChartComponent } from '../components/attendance-chart/attendance-chart.component';
import { DepartmentChartComponent } from '../components/department-chart/department-chart.component';
import { QuickActionsComponent } from '../components/quick-actions/quick-actions.component';
import { UpcomingEventsComponent } from '../components/upcoming-events/upcoming-events.component';
import { PendingApprovalsComponent } from '../components/pending-approvals/pending-approvals.component';
import { RegisterComponent } from '../components/register/register.component';
import { HrDashboardComponent } from '../components/hr-dashboard/hr-dashboard.component';
import { EmployeesDashboardComponent } from '../components/employees-dashboard/employees-dashboard.component';
import { FinanceDashboardComponent } from '../components/finance-dashboard/finance-dashboard.component';
import { ManagerDashboardComponent } from '../components/manager-dashboard/manager-dashboard.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';

import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';

import { MessageService } from 'primeng/api';
import { AuthService } from './auths/auth.service';
import { EmployeelistComponent } from '../components/employeelist/employeelist.component';
import { AttendanceregularizationComponent } from '../components/attendanceregularization/attendanceregularization.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterModule,

    AnnouncementComponent,
    AppreciationComponent,
    AttendanceComponent,
    AttendanceregularizationComponent,
    AuditLogComponent,
    BenefitComponent,
    ComplianceComponent,
    DEIComponent,
    DepartmentComponent,
    DocumentComponent,
    EmployeeBenefitComponent,
    LeaveComponent,
    LeaveTypeComponent,

    PayrollComponent,
    PerformanceReviewComponent,
    SalaryStructureComponent,
    FeedbackComponent,
    TicketComponent,
    TrainingComponent,
    UserComponent,
    HomeComponent,
    LoginComponent,
    DashboardComponent,
    LeaveCalendarComponent,
    SidebarComponent,
    NavbarComponent,
    StatsCardsComponent,
    AttendanceChartComponent,
    DepartmentChartComponent,
    QuickActionsComponent,
    UpcomingEventsComponent,
    PendingApprovalsComponent,
    RegisterComponent,
    HrDashboardComponent,
    EmployeesDashboardComponent,
    FinanceDashboardComponent,
    ManagerDashboardComponent,
    EmployeelistComponent,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,

    MatIconModule,
    MatToolbarModule,
    MatButtonModule,

    TableModule,
    ToastModule,
  ],
  providers: [MessageService],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']  // fixed property name and type
})
export class AppComponent {
  title = 'HRMS';

  @ViewChild(SidebarComponent, { static: false }) sidebar!: SidebarComponent;

  constructor(public authService: AuthService) {}

  get sidebarCollapsed(): boolean {
    return this.sidebar?.isCollapsed ?? false;
  }
}
