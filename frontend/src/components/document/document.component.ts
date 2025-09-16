import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-document',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.css']
})
export class DocumentComponent implements OnInit {
  documentForm!: FormGroup;
  selectedFileName: string | null = null;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.documentForm = this.fb.group({
      id: ['', Validators.required],
      employeeId: ['', Validators.required],
      type: ['', Validators.required],
      expiryDate: [''],
      url: ['', [Validators.pattern('https?://.+')]], // ✅ Not required now
      uploadedAt: ['', Validators.required]
    });
  }

  goBack(): void {
    window.history.back();
  }

  onFileSelected(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files.length > 0) {
      const file = fileInput.files[0];
      this.selectedFileName = file.name;

      // Create a local preview URL (in real app, upload to server)
      const localUrl = URL.createObjectURL(file);
      this.documentForm.patchValue({ url: localUrl });

      // Mark control as touched so validation passes
      this.documentForm.get('url')?.markAsTouched();
    }
  }

  onSubmit(): void {
    if (this.documentForm.invalid) {
      this.documentForm.markAllAsTouched();
      return;
    }

    console.log('Form submitted:', this.documentForm.value);

    // ✅ Show popup message
    alert('✅ Document uploaded successfully!');

    // Reset form
    this.documentForm.reset();
    this.selectedFileName = null;
  }
}
