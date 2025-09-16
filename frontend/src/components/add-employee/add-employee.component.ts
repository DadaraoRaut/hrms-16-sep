import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { User, Department, AuthService } from '../../app/auths/auth.service';
import { SidebarComponent } from '../sidebar/sidebar.component';

@Component({
  selector: 'app-add-employee',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, ToastModule, SidebarComponent],
  templateUrl: './add-employee.component.html',
  styleUrls: ['./add-employee.component.css'],
  providers: [MessageService]
})
export class AddEmployeeComponent implements OnInit {
  employeeForm!: FormGroup;
  selectedPhoto: File | null = null;
  allUsers: User[] = [];
  managers: any[] = [];
  departments: Department[] = [];

  // ✅ Prevent selecting future dates
  maxDate: string = new Date().toISOString().split('T')[0];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.buildForm();
    this.loadAllUsers();
    this.loadDepartments();
    this.loadManagers();
  }

  buildForm() {
    this.employeeForm = this.fb.group({
      userId: ['', Validators.required],
      employeeCode: ['', [Validators.required, Validators.pattern(/^[A-Za-z0-9\-]+$/), Validators.maxLength(10)]],
      firstName: ['', [Validators.required, Validators.pattern(/^[A-Za-z]{2,20}$/)]],
      lastName: ['', [Validators.required, Validators.pattern(/^[A-Za-z]{2,20}$/)]],
      email: ['', [Validators.required, Validators.email]],
      contactNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      gender: ['', Validators.required],
      dob: [''],
      emergencyContact: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      joiningDate: [''],
      probationEndDate: [''],
      exitReason: [''],
      status: ['ACTIVE', Validators.required],
      designation: ['', Validators.required],
      jobType: ['FULL_TIME', Validators.required],
      education: [''],
      experience: [''],
      certifications: [''],
      location: ['', Validators.required],
      managerId: [''],
      departmentId: [null, Validators.required]
    });

    // 🔹 Auto-calculate probation end date
    this.employeeForm.get('joiningDate')?.valueChanges.subscribe(joiningDate => {
      if (joiningDate) {
        const jd = new Date(joiningDate);
        jd.setMonth(jd.getMonth() + 3);
        const probationDate = this.formatDate(jd);
        this.employeeForm.get('probationEndDate')?.setValue(probationDate);
      } else {
        this.employeeForm.get('probationEndDate')?.setValue('');
      }
    });
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

  // 🔹 Restrict year length (max 4 digits)
  restrictYear(event: Event, controlName: string): void {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    if (value) {
      const yearPart = value.split('-')[0];
      if (yearPart.length > 4) {
        this.employeeForm.get(controlName)?.setValue('', { emitEvent: false });
        input.value = '';
        this.messageService.add({
          severity: 'warn',
          summary: 'Invalid Year',
          detail: 'Year cannot exceed 4 digits.'
        });
      }
    }
  }

  loadAllUsers(): void {
    this.authService.getAllUsers().subscribe({
      next: (users) => (this.allUsers = users),
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load users' })
    });
  }

  loadDepartments(): void {
    this.authService.getAllDepartments().subscribe({
      next: (departments) => this.departments = departments,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load departments' })
    });
  }

  loadManagers(): void {
    this.authService.getManagersByEmployee().subscribe({
      next: (managers) => this.managers = managers,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load managers' })
    });
  }

  onPhotoChange(event: any): void {
    if (event.target.files.length > 0) {
      this.selectedPhoto = event.target.files[0];
    }
  }

  allowLettersOnly(event: KeyboardEvent): void {
    const regex = /^[a-zA-Z]$/;
    if (!regex.test(event.key)) event.preventDefault();
  }

  allowNumbersOnly(event: KeyboardEvent): void {
    const regex = /^[0-9]$/;
    if (!regex.test(event.key)) event.preventDefault();
  }

  allowAlphanumeric(event: KeyboardEvent): void {
    const regex = /^[a-zA-Z0-9\-]$/;
    if (!regex.test(event.key)) event.preventDefault();
  }

  submitForm(): void {
    if (this.employeeForm.invalid || !this.selectedPhoto) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Incomplete',
        detail: 'Please fill all required fields correctly and upload a photo.'
      });
      return;
    }

    const formValue = this.employeeForm.value;
    const userId = formValue.userId && formValue.userId !== 'undefined' ? formValue.userId : null;
    const managerId = formValue.managerId ? { empId: Number(formValue.managerId) } : null;

    const payload: any = {
      employeeCode: formValue.employeeCode,
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      email: formValue.email,
      contactNumber: formValue.contactNumber,
      gender: formValue.gender,
      dob: formValue.dob,
      emergencyContact: formValue.emergencyContact,
      joiningDate: formValue.joiningDate,
      probationEndDate: formValue.probationEndDate,
      exitReason: formValue.exitReason,
      status: formValue.status,
      designation: formValue.designation,
      jobType: formValue.jobType,
      education: formValue.education,
      experience: formValue.experience,
      certifications: formValue.certifications,
      location: formValue.location,
      department: { departmentId: formValue.departmentId },
      user: userId ? { userID: userId } : null,
      manager: managerId
    };

    const formData = new FormData();
    formData.append('employee', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
    formData.append('photo', this.selectedPhoto!);

    this.authService.createEmployeeFormData(formData).subscribe({
      next: (res) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: `Employee ${res.firstName} ${res.lastName} created successfully!`
        });
        this.employeeForm.reset();
        this.selectedPhoto = null;
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to create employee.' })
    });
  }

  goBack(): void {
    window.history.back();
  }
}
