import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Project, Employee, AuthService } from '../../app/auths/auth.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css'],
  providers: [MessageService]
})
export class ProjectsComponent implements OnInit {
  projects: Project[] = [];
  projectForm!: FormGroup;
  isEditMode = false;
  selectedProjectId: string = '';
  managers: Employee[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadProjects();
    this.loadManagers();
    this.initForm();
  }

  initForm(): void {
    this.projectForm = this.fb.group({
      projectName: ['', Validators.required],
      description: ['', Validators.required],
      manager: this.fb.group({
        empId: ['', Validators.required]
      }),
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      status: ['IN_PROGRESS', Validators.required]
    });
  }

  loadProjects(): void {
    this.authService.getAllProject().subscribe({
      next: (res) => (this.projects = res),
      error: (err) => this.showError('Failed to load projects')
    });
  }

  loadManagers(): void {
    this.authService.getManagersByEmployee().subscribe({
      next: (res) => (this.managers = res),
      error: () => this.showError('Failed to load managers')
    });
  }

  submit(): void {
    if (this.projectForm.invalid) return;

    const formValue = this.projectForm.value;
    if (this.isEditMode) {
      this.authService.updateProject(this.selectedProjectId, formValue).subscribe({
        next: () => {
          this.showSuccess('Project updated successfully');
          this.resetForm();
          this.loadProjects();
        },
        error: () => this.showError('Update failed')
      });
    } else {
      this.authService.addProject(formValue).subscribe({
        next: () => {
          this.showSuccess('Project created successfully');
          this.resetForm();
          this.loadProjects();
        },
        error: () => this.showError('Creation failed')
      });
    }
  }

  editProject(project: Project): void {
    this.isEditMode = true;
    this.selectedProjectId = project.id!;
    this.projectForm.patchValue(project);
  }

  resetForm(): void {
    this.projectForm.reset();
    this.isEditMode = false;
    this.selectedProjectId = '';
    this.projectForm.get('status')?.setValue('IN_PROGRESS');
  }

  showSuccess(detail: string): void {
    this.messageService.add({ severity: 'success', summary: 'Success', detail });
  }

  showError(detail: string): void {
    this.messageService.add({ severity: 'error', summary: 'Error', detail });
  }
}
