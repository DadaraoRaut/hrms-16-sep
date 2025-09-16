import { Routes } from '@angular/router';
import { LoginComponent } from '../components/login/login.component';
import { RegisterComponent } from '../components/register/register.component';
import { DashboardComponent } from '../components/dashboard/dashboard.component';
import { HomeComponent } from '../components/home/home.component';
import { AttendanceComponent } from '../components/attendance/attendance.component';
import { AnnouncementComponent } from '../components/announcement/announcement.component';
import { AppreciationComponent } from '../components/appreciation/appreciation.component';
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
import { TrainingComponent } from '../components/training/training.component';
import { UserComponent } from '../components/user/user.component';
import { AttendanceChartComponent } from '../components/attendance-chart/attendance-chart.component';
import { LeaveCalendarComponent } from '../components/leave-calendar/leave-calendar.component';
import { QuickActionsComponent } from '../components/quick-actions/quick-actions.component';
import { DepartmentChartComponent } from '../components/department-chart/department-chart.component';
import { UpcomingEventsComponent } from '../components/upcoming-events/upcoming-events.component';
import { PendingApprovalsComponent } from '../components/pending-approvals/pending-approvals.component';
import { HrDashboardComponent } from '../components/hr-dashboard/hr-dashboard.component';
import { FinanceDashboardComponent } from '../components/finance-dashboard/finance-dashboard.component';
import { ManagerDashboardComponent } from '../components/manager-dashboard/manager-dashboard.component';
import { EmployeesDashboardComponent } from '../components/employees-dashboard/employees-dashboard.component';
import { AuthGuard } from './auths/auth.guard';

// ✅ Standalone Components
import { AddEmployeeComponent } from '../components/add-employee/add-employee.component';
import { TicketComponent } from '../components/ticket/ticket.component';
import { BankingComponent } from '../components/banking/banking.component';
import { EmployeelistComponent } from '../components/employeelist/employeelist.component';
import { AttendancelistComponent } from '../components/attendancelist/attendancelist.component';
import { AttendanceregularizationComponent } from '../components/attendanceregularization/attendanceregularization.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // 🔓 Public
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // 📋 Modules
  { path: 'attendance', component: AttendanceComponent },
  { path: 'attendancelist', component: AttendancelistComponent },
  
  { path: 'ttendanceregularization', component: AttendanceregularizationComponent }, // ✅ Fixed
  { path: 'announcement', component: AnnouncementComponent },
  { path: 'appriciation', component: AppreciationComponent },
  { path: 'auditlog', component: AuditLogComponent },
  { path: 'benefit', component: BenefitComponent },
  { path: 'compliance', component: ComplianceComponent },
  { path: 'dei', component: DEIComponent },
  { path: 'department', component: DepartmentComponent },
  { path: 'document', component: DocumentComponent },
  { path: 'employeebenifit', component: EmployeeBenefitComponent },
  { path: 'leave', component: LeaveComponent },
  { path: 'leavetype', component: LeaveTypeComponent },
  { path: 'payroll', component: PayrollComponent },
  { path: 'performance-review', component: PerformanceReviewComponent },
  { path: 'salary-structure', component: SalaryStructureComponent },
  { path: 'Surveyfeedback', component: FeedbackComponent },
  { path: 'ticket', component: TicketComponent },
  { path: 'banking', component: BankingComponent },
  { path: 'training', component: TrainingComponent },
  { path: 'user', component: UserComponent },
  { path: 'leave-calendar', component: LeaveCalendarComponent },
  { path: 'attendance-chart', component: AttendanceChartComponent },
  { path: 'department-chart', component: DepartmentChartComponent },
  { path: 'quick-action', component: QuickActionsComponent },
  { path: 'upcoming-events', component: UpcomingEventsComponent },
  { path: 'pending-approvals', component: PendingApprovalsComponent },
  { path: 'employeelist', component: EmployeelistComponent }, // ✅ Fixed

  // 🎯 Dashboards
  { path: 'hr-dashboard', component: HrDashboardComponent },
  { path: 'employees-dashboard', component: EmployeesDashboardComponent },
  { path: 'finance-dashboard', component: FinanceDashboardComponent },
  { path: 'manager-dashboard', component: ManagerDashboardComponent },

  // 👤 Standalone
  { path: 'add-employee', component: AddEmployeeComponent },

  // 🔐 Protected
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
        children: [
          { path: '', component: HomeComponent }
        ]
      }
    ]
  },

  // ⚠️ Wildcard (optional)
  // { path: '**', redirectTo: 'login' }
];
