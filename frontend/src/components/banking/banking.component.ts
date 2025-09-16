import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { AuthService, Employee } from '../../app/auths/auth.service';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { SidebarComponent } from '../sidebar/sidebar.component';
 
@Component({
  selector: 'app-banking',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ToastModule,
    InputTextModule,
    InputNumberModule,
    SidebarComponent
  ],
  templateUrl: './banking.component.html',
  styleUrls: ['./banking.component.css'],
  providers: [MessageService]
})
export class BankingComponent implements OnInit {
  bankForm!: FormGroup;
  allEmployees: Employee[] = [];
 
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {}
 
  ngOnInit(): void {
    this.buildForm();
    this.loadEmployees();
  }
 
  buildForm() {
    this.bankForm = this.fb.group({
      employeeId: ['', Validators.required],
      accountHolderName: ['', Validators.required],
      bankName: ['', Validators.required],
      accountNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d+$/),
          Validators.minLength(8)
        ]
      ],
      ifscCode: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[A-Z]{4}0[A-Z0-9]{6}$/)
        ]
      ],
      branch: ['', Validators.required],
      accountType: ['', Validators.required]
    });
  }
 
  loadEmployees() {
    this.authService.getAllEmployees().subscribe({
      next: (employees) => {
        this.allEmployees = employees;
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load employees.'
        });
      }
    });
  }
 
  onSubmit() {
    if (this.bankForm.invalid) {
      this.bankForm.markAllAsTouched();
      this.messageService.add({
        severity: 'warn',
        summary: 'Incomplete Form',
        detail: 'Please fill all required fields correctly.'
      });
      return;
    }
 
    const { employeeId, ...payload } = this.bankForm.value;
 
    this.authService.createBankDetails(employeeId, payload).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Bank details saved successfully.'
        });
        this.bankForm.reset();
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to save bank details.'
        });
      }
    });
  }
 
  goBack(): void {
    window.history.back();
  }
}
 
 