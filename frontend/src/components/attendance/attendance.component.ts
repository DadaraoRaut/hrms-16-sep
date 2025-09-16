import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../app/auths/auth.service';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, ToastModule],
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css'],
})
export class AttendanceComponent implements OnInit {
  clockInForm!: FormGroup;
  clockOutForm!: FormGroup;
  regularizeForm!: FormGroup;
  empId: string = '';

   clockedIn: boolean = false;  
    attendanceRequests: any[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.empId = this.authService.getEmployeeIdFromToken();
     this.loadAllRegularizationRequests();

    this.clockInForm = this.fb.group({
      mode: ['WEB', Validators.required],
      location: ['', Validators.required],
      workFrom: ['', Validators.required],
    });

    this.clockOutForm = this.fb.group({
      employeeId: [this.empId, Validators.required],
    });

    this.regularizeForm = this.fb.group({
      date: ['', Validators.required],
      reason: ['', Validators.required],
    });
  }

//   clockIn() {
//   if (this.clockInForm.valid) {
//     const { location, workFrom } = this.clockInForm.value;

//     // Call OpenStreetMap API to convert location to lat/lon
//     this.http.get(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(location)}`)
//       .subscribe({
//         next: (res: any) => {
//           if (res.length > 0) {
//             const lat = parseFloat(res[0].lat);
//             const lon = parseFloat(res[0].lon);

//             const payload = {
//               mode: 'WEB',
//               latitude: lat,
//               longitude: lon,
//               workFrom
//             };

//             this.authService.clockInAttendance(payload).subscribe({
//               next: () =>{
//                 this.clockedIn = true;
//                 this.messageService.add({
//                   severity: 'success',
//                   summary: 'Success',
//                   detail: 'Clocked in successfully!',
//                 });
//               },
//               error: () =>
//                 this.messageService.add({
//                   severity: 'error',
//                   summary: 'Error',
//                   detail: 'Clock-in failed.',
//                 }),
//             });
//           } else {
//             this.messageService.add({
//               severity: 'warn',
//               summary: 'Not Found',
//               detail: 'Unable to get coordinates for the given location.',
//             });
//           }
//         },
//         error: () => {
//           this.messageService.add({
//             severity: 'error',
//             summary: 'API Error',
//             detail: 'Failed to fetch coordinates from location.',
//           });
//         },
//       });
//   }
// }

// clockOutAttendance() {
//   this.authService.clockOutAttendance().subscribe({
//    next: () => {
//         this.clockedIn = false;
//         this.messageService.add({ severity: 'success', summary: 'Clocked Out' });
//       },
//       error: err => {
//         this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error });
//       }
//     });
// }


// submitRegularization() {
//   if (this.regularizeForm.valid) {
//     const date = this.regularizeForm.get('date')?.value;
//     const reason = this.regularizeForm.get('reason')?.value;

//     this.authService.requestRegularization(date, reason).subscribe({
//       next: () => this.messageService.add({ severity: 'success', summary: 'Request Submitted', detail: 'Regularization requested.' }),
//       error: err => this.messageService.add({ severity: 'error', summary: 'Request Failed', detail: err.error || err.message }),
//     });
//   } else {
//     this.messageService.add({ severity: 'warn', summary: 'Form  Invalid', detail: 'Please fill in all required fields.' });
//   } this.loadAllRegularizationRequests(); 
// }


loadAllRegularizationRequests(): void {
    this.authService.getAllRegularizationRequests().subscribe({
      next: (data) => {
        console.log('🔍 Loaded Regularizations:', data);
        this.attendanceRequests = data;
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load regularization requests' });
      }
  });
}

approveRegularizationRequest(attendanceId: number): void {
  this.authService.approveRegularization(attendanceId).subscribe({
    next: (res: string) => {
      this.messageService.add({ 
        severity: 'success', 
        summary: 'Approved', 
        detail: res   // 👈 will show "Regularization approved successfully"
      });
      this.loadAllRegularizationRequests(); // refresh after success
    },
    error: (err) => {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: err.error || 'Approval failed',
      });
    },
  });
}

rejectRegularization(attendanceId: number) {
  const reason = prompt("Enter rejection reason:");

  if (!reason) {
    this.messageService.add({
      severity: 'warn',
      summary: 'Cancelled',
      detail: 'Rejection reason required'
    });
    return;
  }

  this.authService.rejectRegularizationRequest(attendanceId, reason).subscribe({
    next: (res: string) => {
      this.messageService.add({
        severity: 'success',
        summary: 'Rejected',
        detail: res   // 👈 now shows backend message
      });
      this.loadAllRegularizationRequests(); // refresh list
    },
    error: (err: any) => {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: err.error || 'Failed to reject request'
      });
    }
  });
}


 goBack(): void {
  window.history.back();
}

}