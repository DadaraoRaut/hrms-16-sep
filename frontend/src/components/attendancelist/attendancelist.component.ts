import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // adjust path as needed
import { AuthService } from '../../app/auths/auth.service';

@Component({
  selector: 'app-attendancelist',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './attendancelist.component.html',
  styleUrls: ['./attendancelist.component.css']
})
export class AttendancelistComponent implements OnInit {
  attendanceList: any[] = [];
  loading = false;
  errorMessage = '';

  empId: string = ''; // string because AuthService uses string
  fromDate: string = '';
  toDate: string = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Automatically set empId from token if available
    this.empId = this.authService.getEmployeeIdFromToken();
  }

  // Fetch attendance (optional: backend currently returns CSV, so here we just download)
  fetchAttendance(): void {
    if (!this.empId || !this.fromDate || !this.toDate) {
      this.errorMessage = 'Please provide Employee ID, From Date, and To Date';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.downloadAttendanceCsvReport(this.empId, this.fromDate, this.toDate)
      .subscribe({
        next: (blob) => {
          // Automatically trigger download
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `attendance_${this.empId}_${this.fromDate}_to_${this.toDate}.csv`;
          a.click();
          window.URL.revokeObjectURL(url);
          this.loading = false;
        },
        error: (err) => {
          this.errorMessage = 'Failed to fetch attendance';
          console.error(err);
          this.loading = false;
        }
      });
  }

  // Separate download button (optional)
  downloadCSV(): void {
    if (!this.empId || !this.fromDate || !this.toDate) {
      alert('Please provide Employee ID, From Date, and To Date');
      return;
    }

    this.authService.downloadAttendanceCsvReport(this.empId, this.fromDate, this.toDate)
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `attendance_${this.empId}_${this.fromDate}_to_${this.toDate}.csv`;
          a.click();
          window.URL.revokeObjectURL(url);
        },
        error: (err) => {
          console.error('CSV download failed', err);
        }
      });
  }
  
  goBack(): void {
    window.history.back();
  }
}
