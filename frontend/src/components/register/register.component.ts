import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../app/auths/auth.service';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SidebarComponent } from '../sidebar/sidebar.component';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, FormsModule, ToastModule, SidebarComponent],
  providers: [MessageService]
})
export class RegisterComponent {
  user = { username: '', email: '', role: '' };
  roles = ['HR', 'MANAGER', 'FINANCE', 'EMPLOYEE'];
  isLoading = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) {}

  // Username input: allow only lowercase letters and numbers
  allowOnlyLowerAlphaNumeric(event: KeyboardEvent): void {
    const inputChar = event.key;
    if (['Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete'].includes(inputChar)) return;
    if (!/^[a-z0-9]$/.test(inputChar)) event.preventDefault();
  }

  sanitizeUsername(): void {
    if (this.user.username) {
      this.user.username = this.user.username.toLowerCase().replace(/[^a-z0-9]/g, '');
    }
  }

  // Email input: block uppercase letters
  blockUppercaseEmail(event: KeyboardEvent): void {
    const inputChar = event.key;
    if (['Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete'].includes(inputChar)) return;
    if (/[A-Z]/.test(inputChar)) event.preventDefault();
  }

  sanitizeEmail(): void {
    if (this.user.email) this.user.email = this.user.email.toLowerCase();
  }

  // Getter for username regex validation
  get usernamePatternInvalid(): boolean {
  return !!this.user.username && !/^[a-z0-9]+$/.test(this.user.username);
}

  // Getter for email regex validation
  get emailPatternInvalid(): boolean {
    return !! this.user.email && !/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/.test(this.user.email);
  }

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';

    if (!this.authService.isLoggedIn()) {
      this.isLoading = false;
      this.messageService.add({
        severity: 'warn',
        summary: 'Access Denied',
        detail: 'Admin must be logged in to register users.'
      });
      return;
    }

    this.authService.register(this.user).subscribe({
      next: () => {
        this.isLoading = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Registration Successful',
          detail: 'User has been registered successfully!'
        });

        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: err => {
        this.isLoading = false;
        this.errorMessage = err?.error || err?.message || 'Something went wrong.';
        this.messageService.add({
          severity: 'error',
          summary: 'Registration Failed',
          detail: this.errorMessage
        });
      }
    });
  }

  goBack(): void {
    window.history.back();
  }
}
