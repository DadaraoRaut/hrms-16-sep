import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NgChartsModule } from 'ng2-charts';
import { ManagerSidebarComponent } from '../manager-sidebar/manager-sidebar.component';
import { AuthService } from '../../app/auths/auth.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    NgChartsModule,
    ManagerSidebarComponent,
    FormsModule,
    ReactiveFormsModule,
    ToastModule
  ],
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.css'],
  providers: [MessageService]
})
export class ManagerDashboardComponent implements OnInit {
    username: string = '';
  empId: number = 0;
  clockedIn: boolean = false;

  clockInTime!: Date;
  timerDisplay: string = '00:00:00';
  timerInterval: any;
  today: string = new Date().toISOString().split('T')[0];
  currentMonthMin: string = '';
  currentMonthMax: string = '';

  clockInForm!: FormGroup;
  clockOutForm!: FormGroup;
  regularizeForm!: FormGroup;

  totalEmployees = 1245;
  newHiresThisMonth = 42;
  employeesOnLeave = 28;
  attritionRate = 8.5;

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    const currentUser = this.authService.currentUserValue;
    if (currentUser && currentUser.username) {
      this.username = currentUser.username;
    } else {
      const storedUser = localStorage.getItem('currentUser');
      if (storedUser) {
        this.username = JSON.parse(storedUser).username;
      }
    }

    this.authService.getCurrentAttendanceStatus().subscribe({
      next: (res: any) => {
        if (res && res.employee) {
          this.empId = res.employee.empId;
        }

        if (res && res.clockInTime && !res.clockOutTime) {
          this.clockedIn = true;
          const combinedDateTime = `${res.date}T${res.clockInTime}`;
          this.clockInTime = new Date(combinedDateTime);
          this.startTimer();
        } else {
          this.clockedIn = false;
        }
      },
      error: (err) => {
        console.error("Failed to fetch attendance status", err);
      }
    });

    const now = new Date();
    const firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
    const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0);
    this.currentMonthMin = firstDay.toISOString().split('T')[0];
    this.currentMonthMax = lastDay.toISOString().split('T')[0];

    this.clockInForm = this.fb.group({
      workFrom: ['', Validators.required],
      mode: ['', Validators.required]
    });

    this.clockOutForm = this.fb.group({});

    this.regularizeForm = this.fb.group({
      date: ['', [Validators.required]],
      reason: ['', [Validators.required, Validators.pattern(/^[A-Za-z0-9 ]+$/)]],
    });
  }

  restrictYear(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value: string = input.value;

    if (value && value.includes('-')) {
      const parts = value.split('-');
      if (parts[0].length > 4) {
        parts[0] = parts[0].substring(0, 4);
        input.value = parts.join('-');
        this.regularizeForm.get('date')?.setValue(input.value, { emitEvent: false });
      }
    }
  }

  clockIn() {
    if (!this.clockInForm.valid) {
      this.messageService.add({ severity: 'warn', summary: 'Form Invalid', detail: 'Fill all required fields.' });
      return;
    }

    const { workFrom, mode } = this.clockInForm.value;

    if (!navigator.geolocation) {
      this.messageService.add({
        severity: 'error',
        summary: 'Geolocation Not Supported',
        detail: 'Your browser does not support geolocation.'
      });
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;

        const payload = { workFrom, mode, latitude, longitude };

        this.authService.clockInAttendance(payload).subscribe({
          next: (res: any) => {
            this.clockedIn = true;
            this.clockInTime = new Date();
            this.startTimer();

            const clockInTime = this.clockInTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
            this.messageService.add({
              severity: 'success',
              summary: 'Clock-in Successful',
              detail: `${res} at ${clockInTime}. Have a great day!`,
            });
          },
          error: (err) => {
            let errorMsg = 'Clock-in failed';
            if (err.error) {
              if (typeof err.error === 'string') {
                errorMsg = err.error;
              } else if (err.error.message) {
                errorMsg = err.error.message;
              }
            }
            if (errorMsg.toLowerCase().includes('already clocked in')) {
              this.messageService.add({
                severity: 'error',
                summary: 'Clock-in Failed',
                detail: 'You cannot clock in again today.'
              });
            } else {
              this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: errorMsg
              });
            }
          }
        });
      },
      (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Location Error',
          detail: this.getGeolocationErrorMessage(error)
        });
      }
    );
  }

  getGeolocationErrorMessage(error: GeolocationPositionError): string {
    switch (error.code) {
      case error.PERMISSION_DENIED:
        return 'Permission denied. Please allow location access.';
      case error.POSITION_UNAVAILABLE:
        return 'Location information is unavailable.';
      case error.TIMEOUT:
        return 'The request to get location timed out.';
      default:
        return 'An unknown error occurred while fetching location.';
    }
  }

  startTimer() {
    this.updateTimer();
    this.timerInterval = setInterval(() => {
      this.updateTimer();
    }, 1000);
  }

  updateTimer() {
    const now = new Date();
    const diff = now.getTime() - this.clockInTime.getTime();
    const hours = Math.floor(diff / 3600000);
    const minutes = Math.floor((diff % 3600000) / 60000);
    const seconds = Math.floor((diff % 60000) / 1000);
    this.timerDisplay = this.pad(hours) + ':' + this.pad(minutes) + ':' + this.pad(seconds);
  }

  pad(num: number): string {
    return num.toString().padStart(2, '0');
  }

  clockOutAttendance() {
    this.authService.clockOutAttendance().subscribe({
      next: (res: any) => {
        this.clockedIn = false;
        if (this.timerInterval) {
          clearInterval(this.timerInterval);
        }
        this.timerDisplay = '00:00:00';
        const clockOutTime = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        this.messageService.add({
          severity: 'success',
          summary: 'Clock-out Successful',
          detail: `${res || 'You clocked out'} at ${clockOutTime}. Have a nice day!`
        });
      },
      error: (err) =>
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message || 'Clock-out failed'
        })
    });
  }

  submitRegularization() {
    if (!this.regularizeForm.valid) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Form Invalid',
        detail: 'Fill all required fields.'
      });
      return;
    }

    const date = this.regularizeForm.get('date')?.value;
    const reason = this.regularizeForm.get('reason')?.value;

    if (date < this.currentMonthMin || date > this.currentMonthMax) {
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid Date',
        detail: 'Please select a date from the 1st up to today.'
      });
      return;
    }

    this.authService.requestRegularization(date, reason).subscribe({
      next: (res: any) => {
        const requestTime = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        this.messageService.add({
          severity: 'success',
          summary: 'Regularization Submitted',
          detail: `${res || 'Request submitted'} on ${date} at ${requestTime}.`
        });
      },
      error: (err) =>
        this.messageService.add({
          severity: 'error',
          summary: 'Request Failed',
          detail: err.error?.message || err.message
        })
    });
  }

  logout() {
    this.authService.logout();
    window.location.href = '/login';
  }
}

