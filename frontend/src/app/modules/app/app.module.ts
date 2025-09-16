import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';

// ✅ PrimeNG
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MessageService } from 'primeng/api';

// ✅ Standalone components
import { AppComponent } from '../../app.component';
import { LoginComponent } from '../../../components/login/login.component';

// ✅ Interceptor
import { AuthInterceptor } from '../../interceptors/auth.interceptor'; // Adjust path if needed
import { AttendanceChartComponent } from '../../../components/attendance-chart/attendance-chart.component';
import { AttendancelistComponent } from '../../../components/attendancelist/attendancelist.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ToastModule,
    DialogModule,
    FileUploadModule,
    InputTextModule,
    InputNumberModule,
    RouterModule,
    AttendanceChartComponent,
    AppComponent,
    LoginComponent,
    AttendancelistComponent,
  ],
  providers: [
    MessageService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: []
})
export class AppModule {}
