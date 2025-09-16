import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// PrimeNG Modules
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { ToastModule } from 'primeng/toast';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CheckboxModule } from 'primeng/checkbox';
import { TabViewModule } from 'primeng/tabview';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-leave',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CalendarModule,
    DropdownModule,
    InputNumberModule,
    ToastModule,
    TableModule,
    DialogModule,
    ButtonModule,
    InputTextareaModule,
    CheckboxModule,
    TabViewModule
  ],
  templateUrl: './leave.component.html',
  styleUrls: ['./leave.component.css'],
  providers: [MessageService]
})
export class LeaveComponent implements OnInit {
  leaveTypes: any[] = [];
  leaveRequests: any[] = [];
  leaveBalance: any[] = [];
  optionalHolidays: any[] = [];
  selectedHolidays: any[] = [];

  leaveRequest: any = {
    leaveType: null,
    startDate: new Date(),
    endDate: new Date(),
    noOfHalfDays: 0,
    reason: ''
  };

  displayLeaveDialog = false;
  loading = false;
  userRole = 'EMPLOYEE'; // mock role
  employeeId = 'EMP001'; // mock employee ID

  constructor(private messageService: MessageService) {}

  ngOnInit() {
    this.loadLeaveTypes();
    this.loadLeaveBalance();
    this.loadLeaveRequests();
    this.loadOptionalHolidays();
  }

  // Mock Data Loaders
  loadLeaveTypes() {
    this.leaveTypes = [
      { id: 1, name: 'Sick Leave' },
      { id: 2, name: 'Casual Leave' },
      { id: 3, name: 'Earned Leave' }
    ];
  }

  loadLeaveBalance() {
    this.leaveBalance = [
      { leaveType: 'Sick Leave', balance: 5 },
      { leaveType: 'Casual Leave', balance: 7 },
      { leaveType: 'Earned Leave', balance: 12 }
    ];
  }

  loadLeaveRequests() {
    this.leaveRequests = [
      { id: 101, leaveType: 'Sick Leave', startDate: new Date(), endDate: new Date(), status: 'Pending' }
    ];
  }

  loadOptionalHolidays() {
    this.optionalHolidays = [
      { optionalHolidayId: 201, name: 'Festival Holiday 1' },
      { optionalHolidayId: 202, name: 'Festival Holiday 2' }
    ];
  }

  // Actions
  submitLeaveRequest() {
    if (!this.leaveRequest.leaveType) return;

    const newRequest = {
      id: Date.now(),
      leaveType: this.leaveRequest.leaveType.name,
      startDate: this.leaveRequest.startDate,
      endDate: this.leaveRequest.endDate,
      noOfHalfDays: this.leaveRequest.noOfHalfDays,
      reason: this.leaveRequest.reason,
      status: 'Pending'
    };

    this.leaveRequests.push(newRequest);

    this.messageService.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Leave request submitted successfully'
    });

    this.displayLeaveDialog = false;
    this.resetLeaveRequest();
  }

  resetLeaveRequest() {
    this.leaveRequest = {
      leaveType: null,
      startDate: new Date(),
      endDate: new Date(),
      noOfHalfDays: 0,
      reason: ''
    };
  }

  selectOptionalHolidays() {
    if (this.selectedHolidays.length === 0) return;

    this.messageService.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Holiday request submitted successfully'
    });

    this.selectedHolidays = [];
  }

  approveRejectLeave(leaveRequestId: number, status: string) {
    const req = this.leaveRequests.find(l => l.id === leaveRequestId);
    if (req) {
      req.status = status;
      this.messageService.add({
        severity: 'success',
        summary: 'Success',
        detail: `Leave request ${status.toLowerCase()} successfully`
      });
    }
  }

  goBack(): void {
    window.history.back();
  }
}
