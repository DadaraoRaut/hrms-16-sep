import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-upcoming-events',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './upcoming-events.component.html',
  styleUrls: ['./upcoming-events.component.css']
})
export class UpcomingEventsComponent {
  events = [
    {
      title: '5 Employee Birthdays',
      date: 'Today',
      dateClass: '',
      description: 'John D., Sarah M., Alex P., Lisa T., Mark R.'
    },
    {
      title: 'Payroll Processing',
      date: 'Due in 2 days',
      dateClass: 'text-danger',
      description: 'Monthly payroll processing for June 2023'
    },
    {
      title: 'Quarterly Performance Reviews',
      date: 'Starts next week',
      dateClass: 'text-warning',
      description: 'Q2 2023 performance review cycle'
    },
    {
      title: 'DEI Training Session',
      date: 'June 15',
      dateClass: '',
      description: 'Mandatory for all managers'
    }
  ];
  
}