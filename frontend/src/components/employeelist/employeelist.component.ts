import { Component, OnInit } from '@angular/core';
import { AuthService, Employee } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';

@Component({
  selector: 'app-employeelist',
  templateUrl: './employeelist.component.html',
  styleUrls: ['./employeelist.component.css'],
  standalone: true,
  imports: [CommonModule, SidebarComponent] 
})
export class EmployeelistComponent implements OnInit {

  employees: Employee[] = [];
  loading = true;
  errorMessage = '';
 

  constructor(private authService: AuthService,
     private router: Router 
  ) {}

  ngOnInit(): void {
    this.fetchEmployees();
  }
  getLatestSalary(emp: Employee): number | null {
  if (emp.salaryStructures && emp.salaryStructures.length > 0) {
    return emp.salaryStructures[emp.salaryStructures.length - 1].basicSalary;
  }
  return null;
}


  fetchEmployees(): void {
    this.authService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching employees:', err);
        this.errorMessage = 'Failed to load employees';
        this.loading = false;
      }
    });
  }
  goBack(): void {
    window.history.back();
  }
}
