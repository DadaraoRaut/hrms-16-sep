import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Needed for basic Angular directives like *ngIf, *ngFor
import { MessageService } from 'primeng/api'; // Import MessageService
import { TableModule } from 'primeng/table'; // Import TableModule for p-table
import { ToastModule } from 'primeng/toast'; // Import ToastModule for p-toast
 
@Component({
  selector: 'app-pending-approvals',
  standalone: true, // <--- Mark this component as standalone
  imports: [
    CommonModule, // Required for common Angular directives
    TableModule,  // To recognize the <p-table> element
    ToastModule   // To recognize the <p-toast> element
  ],
  templateUrl: './pending-approvals.component.html',
  styleUrls: ['./pending-approvals.component.css'],
  providers: [MessageService] // Provide MessageService if not provided globally
})
export class PendingApprovalsComponent {
  approvals = [
    {
      type: 'Leave',
      typeClass: 'bg-warning text-dark',
      employee: 'John Doe',
      details: 'Sick Leave - 3 days',
      date: 'Jun 5-7, 2023',
      status: 'Pending',
      statusClass: 'bg-info'
    },
    {
      type: 'Expense',
      typeClass: 'bg-primary',
      employee: 'Sarah Smith',
      details: 'Travel Reimbursement - $250',
      date: 'May 28, 2023',
      status: 'Pending',
      statusClass: 'bg-info'
    },
    {
      type: 'Timesheet',
      typeClass: 'bg-success',
      employee: 'Mike Johnson',
      details: 'Week 22 - 45 hours',
      date: 'May 29-Jun 4',
      status: 'Pending',
      statusClass: 'bg-info'
    },
    {
      type: 'POSH',
      typeClass: 'bg-danger',
      employee: 'Anonymous',
      details: 'Harassment Complaint',
      date: 'Jun 1, 2023',
      status: 'Urgent',
      statusClass: 'bg-warning'
    }
  ];
 
  constructor(private messageService: MessageService) {}
 
  approveRequest(index: number) {
    const approval = this.approvals[index];
    this.messageService.add({
      severity: 'success',
      summary: 'Approved',
      detail: `${approval.type} request for ${approval.employee} approved`
    });
    this.approvals.splice(index, 1);
  }
 
  rejectRequest(index: number) {
    const approval = this.approvals[index];
    this.messageService.add({
      severity: 'error',
      summary: 'Rejected',
      detail: `${approval.type} request for ${approval.employee} rejected`
    });
    this.approvals.splice(index, 1);
  }
}
 
 