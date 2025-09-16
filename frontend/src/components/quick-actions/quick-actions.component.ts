import { Component } from '@angular/core';

import { MessageService } from 'primeng/api';

import { CommonModule } from '@angular/common'; // Needed for standalone components using common directives

import { ToastModule } from 'primeng/toast'; // Needed if p-toast is used in this component's template
 
@Component({

  selector: 'app-quick-actions',

  standalone: true, // <--- ADD THIS LINE

  imports: [

    CommonModule,

    ToastModule // Add ToastModule here if your quick-actions.component.html contains <p-toast>

  ],

  templateUrl: './quick-actions.component.html',

  styleUrls: ['./quick-actions.component.css'],

  providers: [MessageService] // Keep this if MessageService is not provided globally

})

export class QuickActionsComponent {

  actions = [

    { icon: 'fas fa-user-plus', label: 'Add Employee', color: 'primary' },

    { icon: 'fas fa-file-invoice-dollar', label: 'Run Payroll', color: 'success' },

    { icon: 'fas fa-chart-bar', label: 'Generate Report', color: 'info' },

    { icon: 'fas fa-bullhorn', label: 'Post Announcement', color: 'warning' },

    { icon: 'fas fa-tasks', label: 'Start Review Cycle', color: 'danger' },

    { icon: 'fas fa-book', label: 'Assign Training', color: 'secondary' }

  ];
 
  constructor(private messageService: MessageService) {}
 
  actionClicked(action: string) {

    this.messageService.add({

      severity: 'info',

      summary: 'Action',

      detail: `${action} clicked`

    });

  }

}
 