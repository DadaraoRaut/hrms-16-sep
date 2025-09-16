import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService, Compliance } from '../../app/auths/auth.service';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Router } from '@angular/router';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@Component({
  selector: 'app-compliance',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ConfirmDialogModule],
  templateUrl: './compliance.component.html',
  styleUrls: ['./compliance.component.css'],
  providers: [MessageService, ConfirmationService]
})
export class ComplianceComponent implements OnInit {
  complianceForm!: FormGroup;
  compliances: Compliance[] = [];
  editMode = false;
  selectedComplianceId: string | null = null;
  isLoading = false;

  frequencies = ['QUARTERLY', 'MONTHLY', 'YEARLY'];
  types = ['STATUTORY', 'INTERNAL', 'REGULATORY'];

  // ✅ restrict future dates
  maxDate: string = new Date().toISOString().split('T')[0];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadCompliances();
  }

  initForm(): void {
    this.complianceForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(25), Validators.pattern(/^[A-Za-z\s]+$/)]],
      description: ['', [Validators.required, Validators.maxLength(100)]],
      type: ['', Validators.required],
      frequency: ['', Validators.required],
      dueDate: ['', Validators.required],
      penalty: ['', [Validators.required, Validators.pattern(/^[0-9]+$/), Validators.maxLength(5)]],
      documentRequired: [false],
      isActive: [true]
    });
  }

  // ✅ restrict year length
  restrictYear(event: Event, controlName: string): void {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    if (value) {
      const yearPart = value.split('-')[0];
      if (yearPart.length > 4) {
        this.complianceForm.get(controlName)?.setValue('', { emitEvent: false });
        input.value = '';
        this.messageService.add({
          severity: 'warn',
          summary: 'Invalid Year',
          detail: 'Year cannot exceed 4 digits.'
        });
      }
    }
  }

  allowOnlyLetters(event: KeyboardEvent) {
    if (!/^[A-Za-z\s]$/.test(event.key)) {
      event.preventDefault();
    }
  }

  allowOnlyNumbers(event: KeyboardEvent) {
    if (!/^[0-9]$/.test(event.key)) {
      event.preventDefault();
    }
  }

  isInvalid(controlName: string): boolean {
    const control = this.complianceForm.get(controlName);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  loadCompliances(): void {
    this.authService.getAllCompliance().subscribe({
      next: (data) => (this.compliances = data),
      error: () =>
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load compliances'
        })
    });
  }

  confirmSubmit(): void {
    if (this.complianceForm.invalid) {
      this.complianceForm.markAllAsTouched();
      this.messageService.add({
        severity: 'warn',
        summary: 'Validation',
        detail: 'Please fill in all required fields.'
      });
      return;
    }

    this.confirmationService.confirm({
      message: this.editMode
        ? 'Are you sure you want to update this compliance?'
        : 'All fields are filled. Do you want to create this compliance?',
      header: this.editMode ? 'Confirm Update' : 'Confirm Create',
      icon: 'pi pi-check-circle',
      accept: () => {
        this.onSubmit();
      }
    });
  }

  private onSubmit(): void {
    this.isLoading = true;
    const complianceData: Compliance = this.complianceForm.value;

    if (this.editMode && this.selectedComplianceId) {
      this.authService.updateCompliance(this.selectedComplianceId, complianceData).subscribe({
        next: () => {
          this.isLoading = false;
          this.messageService.add({
            severity: 'success',
            summary: 'Updated',
            detail: 'Compliance updated successfully.'
          });
          this.resetForm();
          this.loadCompliances();
        },
        error: () => {
          this.isLoading = false;
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to update compliance.'
          });
        }
      });
    } else {
      this.authService.addComplaince(complianceData).subscribe({
        next: () => {
          this.isLoading = false;
          this.messageService.add({
            severity: 'success',
            summary: 'Created',
            detail: 'Compliance added successfully.'
          });
          this.resetForm();
          this.loadCompliances();
        },
        error: () => {
          this.isLoading = false;
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to create compliance.'
          });
        }
      });
    }
  }

  onEdit(compliance: Compliance): void {
    if (!compliance.complianceId) return;
    this.selectedComplianceId = compliance.complianceId;
    this.complianceForm.patchValue(compliance);
    this.editMode = true;
  }

  onDelete(id: string): void {
    if (confirm('Are you sure you want to delete this compliance?')) {
      this.authService.deleteComplaince(id).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Deleted',
            detail: 'Compliance deleted successfully.'
          });
          this.loadCompliances();
        },
        error: () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to delete compliance.'
          });
        }
      });
    }
  }

  resetForm(): void {
    this.complianceForm.reset({
      documentRequired: false,
      isActive: true
    });
    this.editMode = false;
    this.selectedComplianceId = null;
  }

  goBack(): void {
    window.history.back();
  }
}
