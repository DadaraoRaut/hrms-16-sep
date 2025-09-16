import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService, Employee } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@Component({
  selector: 'app-salary-structure',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    ToastModule,
    ConfirmDialogModule
  ],
  templateUrl: './salary-structure.component.html',
  styleUrls: ['./salary-structure.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class SalaryStructureComponent implements OnInit {
  salaryForm!: FormGroup;
  fetchedData: any = null;
  updateMode = false;
  employees: Employee[] = [];

  // ✅ Max date (used in template to prevent future dates)
  maxDate: string = new Date().toISOString().split('T')[0];

  salaryFields = [
    { name: 'basicSalary', label: 'Basic Salary', placeholder: 'Enter basic salary' },
    { name: 'hra', label: 'HRA', placeholder: 'Enter HRA' },
    { name: 'specialAllowance', label: 'Special Allowance', placeholder: 'Enter special allowance' },
    { name: 'bonus', label: 'Bonus', placeholder: 'Enter bonus' },
    { name: 'pfDeduction', label: 'PF Deduction', placeholder: 'Enter PF deduction' },
    { name: 'taxDeduction', label: 'Tax Deduction', placeholder: 'Enter tax deduction' }
  ];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.salaryForm = this.fb.group({
      salaryStructureId: [''],
      employeeId: ['', Validators.required],
      basicSalary: ['', [Validators.required, Validators.min(0)]],
      hra: ['', [Validators.required, Validators.min(0)]],
      specialAllowance: ['', [Validators.required, Validators.min(0)]],
      bonus: ['', [Validators.required, Validators.min(0)]],
      pfDeduction: ['', [Validators.required, Validators.min(0)]],
      taxDeduction: ['', [Validators.required, Validators.min(0)]],
      effectiveFrom: ['', Validators.required],
      effectiveTo: ['', Validators.required],
    });

    // 🔹 Watch for negatives and reset them to 0
    this.salaryFields.forEach(field => {
      this.salaryForm.get(field.name)?.valueChanges.subscribe(value => {
        if (value < 0) {
          this.salaryForm.get(field.name)?.setValue(0, { emitEvent: false });
        }
      });
    });

    this.loadEmployees();
  }

  loadEmployees(): void {
    this.authService.getAllEmployees().subscribe({
      next: (res) => this.employees = res,
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load employees' });
      }
    });
  }

  assignSalary() {
    if (this.salaryForm.invalid) {
      this.salaryForm.markAllAsTouched();
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Please fill all required fields correctly.' });
      return;
    }

    const employeeId = this.salaryForm.get('employeeId')?.value;
    const payload = this.getFormPayload();

    this.authService.assignSalaryStructure(employeeId, payload).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Salary structure created!' });
        this.salaryForm.reset();
        this.updateMode = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to assign salary structure.' });
      }
    });
  }

  fetchSalaryStructureByEmpId() {
    const empId = this.salaryForm.get('employeeId')?.value;
    if (!empId) {
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Please select an employee.' });
      return;
    }

    this.authService.getSalaryStructure(empId).subscribe({
      next: (data: any) => {
        if (data && data.id) {
          this.fetchedData = data;
          this.salaryForm.patchValue({
            salaryStructureId: data.id,
            basicSalary: data.basicSalary,
            hra: data.hra,
            specialAllowance: data.specialAllowance,
            bonus: data.bonus,
            pfDeduction: data.pfDeduction,
            taxDeduction: data.taxDeduction,
            effectiveFrom: data.effectiveFrom,
            effectiveTo: data.effectiveTo
          });
          this.updateMode = true;
        } else {
          this.messageService.add({ severity: 'info', summary: 'Not found', detail: 'No salary structure found for this employee.' });
          this.salaryForm.patchValue({ salaryStructureId: '' });
          this.fetchedData = null;
          this.updateMode = false;
        }
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch salary structure.' });
      }
    });
  }

  updateSalaryStructure() {
    if (this.salaryForm.invalid) {
      this.salaryForm.markAllAsTouched();
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Please fill all required fields correctly.' });
      return;
    }

    const id = this.salaryForm.get('salaryStructureId')?.value;
    const empId = this.salaryForm.get('employeeId')?.value;
    if (!id || !empId) return;

    const payload = this.getFormPayload();

    this.authService.updateSalaryStructure(empId, payload).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Updated', detail: 'Salary structure updated successfully!' });
        this.salaryForm.reset();
        this.updateMode = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update salary structure.' });
      }
    });
  }

  confirmDelete() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete this salary structure?',
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      accept: () => this.deleteSalaryStructure()
    });
  }

  deleteSalaryStructure() {
    const id = this.salaryForm.get('salaryStructureId')?.value;
    if (!id) {
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'No salary structure selected to delete.' });
      return;
    }

    this.authService.deleteSalaryStructure(id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Salary structure deleted successfully!' });
        this.salaryForm.reset();
        this.updateMode = false;
      },
      error: (err) => {
        if (err.status === 200 || err.status === 204) {
          this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Salary structure deleted successfully!' });
          this.salaryForm.reset();
          this.updateMode = false;
        } else {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete salary structure.' });
        }
      }
    });
  }

  onSubmit() {
    if (this.updateMode) {
      this.confirmationService.confirm({
        message: 'Are you sure you want to update this salary structure?',
        header: 'Confirm Update',
        icon: 'pi pi-check',
        accept: () => this.updateSalaryStructure()
      });
    } else {
      this.confirmationService.confirm({
        message: 'Are you sure you want to assign this salary structure?',
        header: 'Confirm Assign',
        icon: 'pi pi-check',
        accept: () => this.assignSalary()
      });
    }
  }

  private getFormPayload() {
    return {
      basicSalary: Number(this.salaryForm.get('basicSalary')?.value),
      hra: Number(this.salaryForm.get('hra')?.value),
      specialAllowance: Number(this.salaryForm.get('specialAllowance')?.value),
      bonus: Number(this.salaryForm.get('bonus')?.value),
      pfDeduction: Number(this.salaryForm.get('pfDeduction')?.value),
      taxDeduction: Number(this.salaryForm.get('taxDeduction')?.value),
      effectiveFrom: this.salaryForm.get('effectiveFrom')?.value,
      effectiveTo: this.salaryForm.get('effectiveTo')?.value
    };
  }

  goBack(): void {
    window.history.back();
  }

  // ✅ Restrict typing more than 4 digits in year
  // ✅ Restrict year to exactly 4 digits at the end of dd/mm/yyyy
restrictYearInput(event: KeyboardEvent) {
  const input = event.target as HTMLInputElement;
  let value = input.value;

  // Allow digits and slash only
  if (!/[0-9/]/.test(event.key)) {
    event.preventDefault();
    return;
  }

  // Predict the value after this keypress
  const predicted = value.substring(0, input.selectionStart || 0) + event.key + value.substring(input.selectionEnd || 0);

  // Match dd/mm/yyyy format
  const parts = predicted.split('/');
  if (parts.length === 3) {
    const year = parts[2];
    if (year.length > 4) {
      event.preventDefault();
      this.messageService.add({
        severity: 'warn',
        summary: 'Invalid Year',
        detail: 'Year must be exactly 4 digits.'
      });
    }
  }
}

}
