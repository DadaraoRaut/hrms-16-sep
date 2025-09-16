import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService, Benefit } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-benefit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ToastModule],
  templateUrl: './benefit.component.html',
  providers: [MessageService]
})
export class BenefitComponent implements OnInit {
  benefitForm!: FormGroup;
  benefits: any[] = [];
  isEdit: boolean = false;
  editingId: string | null = null;

  benefitTypes = ['REIMBURSEMENT', 'MONETARY', 'NON_MONETARY'];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadAllBenefits();
  }

  initForm(): void {
    this.benefitForm = this.fb.group({
      name: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[A-Za-z\s]+$/),
          Validators.maxLength(20)
        ]
      ],
      description: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],
      type: ['', Validators.required],
      isTaxable: [false]
    });
  }

  allowOnlyLetters(event: KeyboardEvent) {
    const char = String.fromCharCode(event.keyCode);
    if (!/^[A-Za-z\s]$/.test(char)) {
      event.preventDefault();
    }
  }

  loadAllBenefits(): void {
    this.authService.getAllBenefits().subscribe({
      next: (res) => (this.benefits = res),
      error: () => this.showMessage('error', 'Error', 'Failed to load benefits')
    });
  }

  onSubmit(): void {
    if (this.benefitForm.invalid) {
      this.benefitForm.markAllAsTouched();
      return;
    }

    const benefit: Benefit = this.benefitForm.value;

    if (this.isEdit && this.editingId) {
      this.authService.updateBenefit(this.editingId, benefit).subscribe({
        next: () => {
          this.showMessage('success', 'Updated', 'Benefit updated successfully');
          this.resetForm();
          this.loadAllBenefits();
        },
        error: () => {
          this.showMessage('error', 'Error', 'Failed to update benefit');
        }
      });
    } else {
      this.authService.addBenefit(benefit).subscribe({
        next: () => {
          this.showMessage('success', 'Created', 'Benefit created successfully');
          this.resetForm();
          this.loadAllBenefits();
        },
        error: () => {
          this.showMessage('error', 'Error', 'Failed to create benefit');
        }
      });
    }
  }

  editBenefit(b: any): void {
    this.benefitForm.patchValue(b);
    this.isEdit = true;
    this.editingId = b.benefitId;
  }

  deleteBenefit(id: string): void {
    if (confirm('Are you sure you want to delete this benefit?')) {
      this.authService.deleteBenefits(id).subscribe({
        next: () => {
          this.showMessage('success', 'Deleted', 'Benefit deleted');
          this.loadAllBenefits();
        },
        error: () => {
          this.showMessage('error', 'Error', 'Failed to delete benefit');
        }
      });
    }
  }

  resetForm(): void {
    this.benefitForm.reset({ isTaxable: false });
    this.isEdit = false;
    this.editingId = null;
  }

  showMessage(severity: string, summary: string, detail: string) {
    this.messageService.add({ severity, summary, detail });
  }

  goBack(): void {
    window.history.back();
  }
}
