import { Component, OnInit } from '@angular/core';
import { AuthService, Payroll, Employee, User } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
 
@Component({
  selector: 'app-payroll',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './payroll.component.html',
  styleUrls: ['./payroll.component.css'],
})
export class PayrollComponent implements OnInit {
  form!: FormGroup;
  payroll?: Payroll;
  errorMessage: string = '';
  allEmployees: Employee[] = [];
  maxYear: number = new Date().getFullYear();
  currentUser!: User | null;
  isHrOrAdmin: boolean = false;
  currentEmployeeName: string = '';
  isFormReady = false;
 
  months = [
    { name: 'Jan', value: 'Jan' }, { name: 'Feb', value: 'Feb' }, { name: 'Mar', value: 'Mar' },
    { name: 'Apr', value: 'Apr' }, { name: 'May', value: 'May' }, { name: 'Jun', value: 'Jun' },
    { name: 'Jul', value: 'Jul' }, { name: 'Aug', value: 'Aug' }, { name: 'Sep', value: 'Sep' },
    { name: 'Oct', value: 'Oct' }, { name: 'Nov', value: 'Nov' }, { name: 'Dec', value: 'Dec' }
  ];
  years: number[] = [];
 
  constructor(
    private payrollService: AuthService,
    private fb: FormBuilder
  ) {}
 
  ngOnInit() {
    this.populateYears();
 
    this.form = this.fb.group({
      employeeId: ['', Validators.required],
      month: ['', Validators.required],
      year: ['', Validators.required]
    });
 
    this.currentUser = this.payrollService.currentUserValue;
    this.isHrOrAdmin = this.currentUser?.role === 'ADMIN' || this.currentUser?.role === 'HR';
 
    if (this.isHrOrAdmin) {
      this.loadEmployees();
      this.isFormReady = true; // HR/Admin can use immediately
    } else {
      this.loadNormalEmployee();
    }
  }
 
private loadNormalEmployee() {
  this.payrollService.getAllEmployees().subscribe({
    next: (employees) => {
      this.allEmployees = employees || [];
      console.log('Current User:', this.currentUser);
      console.log('All Employees:', this.allEmployees);

      let matchedEmployee = this.allEmployees.find(emp =>
        emp.user?.userID === this.currentUser?.userID
      );

      if (!matchedEmployee) {
        matchedEmployee = this.allEmployees.find(emp =>
          emp.user?.username?.toLowerCase() === this.currentUser?.username?.toLowerCase()
        );
      }

      if (matchedEmployee) {
        this.form.patchValue({ employeeId: matchedEmployee.empId });
        this.form.controls['employeeId'].disable();
        this.currentEmployeeName = `${matchedEmployee.firstName} ${matchedEmployee.lastName}`;
        this.isFormReady = true;
      } else {
        this.errorMessage = 'Employee record not found.';
        this.isFormReady = false;
      }
    },
    error: (err) => {
      this.errorMessage = err.error?.message || err.message || 'Failed to load employees';
      this.isFormReady = false;
    }
  });
}

 
 
 
  populateYears() {
    const startYear = 2000;
    for (let y = this.maxYear; y >= startYear; y--) {
      this.years.push(y);
    }
  }
 
  private loadEmployees() {
    this.payrollService.getAllEmployees().subscribe({
      next: (data) => { this.allEmployees = data || []; },
      error: (err) => { this.errorMessage = err.error?.message || err.message || 'Failed to load employees'; }
    });
  }
 
  private isFormValid(): boolean {
    if (!this.isFormReady) return false;
 
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.errorMessage = 'Please fill all fields correctly.';
      return false;
    }
 
    const employeeId = this.isHrOrAdmin
      ? this.form.get('employeeId')?.value
      : this.form.getRawValue().employeeId;
 
    if (!employeeId) {
      this.errorMessage = 'Employee ID is missing.';
      return false;
    }
 
    this.errorMessage = '';
    return true;
  }
 
  generate() {
    if (!this.isFormValid()) return;
 
    const { employeeId, month, year } = this.form.getRawValue();
    this.payrollService.generatePayroll(employeeId, month, year).subscribe({
      next: (payroll) => { this.payroll = payroll; },
      error: (err) => {
        this.errorMessage = err.error?.message || err.message || 'Error generating payroll';
        this.payroll = undefined;
      }
    });
  }
 
  viewPayroll() {
    if (!this.isFormValid()) return;
 
    const employeeId = this.isHrOrAdmin
      ? this.form.get('employeeId')?.value
      : this.form.getRawValue().employeeId;
 
    const { month, year } = this.form.getRawValue();
 
    this.payrollService.viewPayroll(employeeId, month, year).subscribe({
      next: (payroll) => {
        this.payroll = payroll;
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = err.error?.message || err.message || 'Error fetching payroll';
        this.payroll = undefined;
      }
    });
  }
 
  downloadPayslip() {
    if (!this.isFormValid()) return;
 
    const employeeId = this.isHrOrAdmin
      ? this.form.get('employeeId')?.value
      : this.form.getRawValue().employeeId;
 
    const { month, year } = this.form.getRawValue();
 
    this.payrollService.downloadPayslip(employeeId, month, year).subscribe({
      next: (response) => {
        const blob = new Blob([response.body!], { type: 'application/pdf' });
        const employee = this.allEmployees.find(e => e.empId?.toString() === employeeId?.toString());
        let filename = employee
          ? `${employee.firstName}_${employee.lastName}(${employeeId})_${month}_${year}.pdf`
          : `payslip_${employeeId}_${month}_${year}.pdf`;
 
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || err.message || 'Error downloading payslip';
      }
    });
  }
 
  goBack(): void {
    window.history.back();
  }
}
 
 