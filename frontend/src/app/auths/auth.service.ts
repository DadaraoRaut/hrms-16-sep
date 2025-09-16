import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, map, of, switchMap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../components/environment';


export interface Department {
  departmentId?: number;
  departmentCode: string;
  name: string;
  description?: string;
  location?: string;
  departmentHead?: {
    employeeCode: any;
    lastName: any;
    firstName: any; empId: number 
} | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface User {
  empId: any;
  // id?: string;
  userID?: string | number;
  username: string;
  email: string;
  role: string;
  status?: string;
}
export interface SalaryStructure {
  id: number;
  basicSalary: number;
  hra: number;
  specialAllowance: number;
  bonus: number;
  pfDeduction: number;
  taxDeduction: number;
  effectiveFrom: string;
  effectiveTo: string;
  status: string;
  createdDate: string;
}

export interface Employee {
  empId: number;
  createdBy?: string;
  employeeCode: string;
  firstName: string;
  lastName: string;
  photo?: string;
  gender?: string;
  dob?: string;
  contactNumber: string;
  email: string;
  emergencyContact?: string;
  joiningDate?: string;
  probationEndDate?: string;
  resignationDate?: string | null;
  exitReason?: string | null;
  status?: string;
  designation: string;
  jobType?: string;
  location?: string;
  education?: string;
  experience?: string;
  certifications?: string;
  createdAt?: string;
  updatedAt?: string;
  department?: any;
  manager?: any;
  // user?: { username: string};
   user?: User;
  benefits?: any[];
  salaryStructures?: SalaryStructure[];
  leaves?: any[];
}

export interface Payroll {
  id: number;
  employee: any;  // you can create Employee interface if needed
  month: string;
  year: number;
  generatedDate: string;
  totalEarnings: number;
  totalDeductions: number;
  netSalary: number;
  paymentStatus: string;
  presentDays: number;
  paidLeaveDays: number;
  workingDays: number;
  payableDays: number;
}

export interface LeaveType {
  leaveTypeId: number;
  name: string;
  carryForward: boolean;
  encashable: boolean;
  maxDaysPerYear: number;
  leaves: any[]; // you can later replace with a Leave[] interface if you want
  approvalFlow: string;
  description: string;
}

export interface Project {
  id?: string;
  projectName: string;
  description: string;
  startDate: string;
  endDate: string;
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED' | 'ON_HOLD';
  manager?: {
    empId: string;
    fullName?: string;
  };
}

export interface Compliance {
  complianceId?: string;
  name: string;
  description: string;
  type: 'STATUTORY' | 'INTERNAL' | 'OTHER'; // Optional enum if predefined
  frequency: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'YEARLY'; // Optional enum
  dueDate: string; // ISO format e.g., '2025-07-05'
  penalty: string;
  documentRequired: boolean;
  isActive: boolean;
}

export interface Benefit {
  name: string;
  description: string;
  type: 'INSURANCE' | 'RETIREMENT' | 'WELLNESS' | 'TRANSPORT' | 'OTHER'
  isTaxable: boolean
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  getLoggedInEmployee(): Employee {
    throw new Error('Method not implemented.');
  }
  getUserInfo() {
    throw new Error('Method not implemented.');
  }
  private apiUrl = environment.apiBaseUrl;
  private currentUserSubject: BehaviorSubject<any>;
  public currentUser: Observable<any>;
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    private router: Router,
    private messageService: MessageService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
    const storedUser = this.isBrowser ? localStorage.getItem('currentUser') : null;
    this.currentUserSubject = new BehaviorSubject<any>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue() {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    if (!this.isBrowser) return null;
    const stored = localStorage.getItem('token');


    // Try to parse in case it was stored as {"token": "..."} by mistake
    try {
      const parsed = JSON.parse(stored || '');
      return typeof parsed === 'string' ? parsed : parsed.token;
    } catch {
      return stored;
    }
  }



  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    console.log('Sending createDepartment with token:', this.getToken());
    const headersConfig: any = { 'Content-Type': 'application/json' };
    if (token) headersConfig['Authorization'] = `Bearer ${token}`;
    return new HttpHeaders(headersConfig);
  }

  getMultipartAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }


  isAdmin(): boolean {
    return this.getUserRole() === 'ADMIN';
  }

  isHR(): boolean {
    return this.getUserRole() === 'HR';
  }

  isManager(): boolean {
    return this.getUserRole() === 'MANAGER';
  }

  isEmployee(): boolean {
    return this.getUserRole() === 'EMPLOYEE';
  }

  isLoggedIn(): boolean {

    return this.isBrowser && localStorage.getItem('token') !== null;
  }

  logout(): void {
  if (this.isBrowser) {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    localStorage.removeItem('employeeId');
  }
  this.currentUserSubject.next(null);

  this.messageService.add({
    severity: 'info',
    summary: 'Logout',
    detail: 'You have been logged out',
  });

  // Redirect and prevent navigating back to dashboard
  this.router.navigate(['/login']).then(() => {
    window.history.pushState(null, '', window.location.href);
    window.onpopstate = () => {
      window.history.go(1);
    };
  });
}


  private handleError(error: any, message: string): Observable<never> {
    const errMsg = error?.error || error?.message || 'Unknown error';
    this.messageService.add({
      severity: 'error',
      summary: message,
      detail: errMsg,
    });
    return throwError(() => error);
  }

  // ✅ Admin Login
  adminLogin(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/admin/login`, { username, password }, {
      responseType: 'text'
    }).pipe(
      map((token: string) => {
        if (this.isBrowser) {
          localStorage.setItem('token', token);
          const user = { username, role: 'ADMIN' };
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);
        }

        this.messageService.add({
          severity: 'success',
          summary: 'Admin Login',
          detail: 'Login successful',
        });
        this.router.navigate(['/dashboard']);
        return { token };
      }),
      catchError((error) => this.handleError(error, 'Admin login failed'))
    );
  }

  // ✅ Updated User Login
  userLogin(username: string, password: string): Observable<any> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/users/login`, { username, password }).pipe(
      switchMap((response) => {
        const token = response.token;
        if (this.isBrowser) {
          localStorage.setItem('token', token);
          const payload = JSON.parse(atob(token.split('.')[1]));
          const rawRole = payload.Role || payload.role || '';
          const normalizedRole = rawRole.replace('ROLE_', '').toUpperCase();

          const user = {
            username: payload.sub,
            role: normalizedRole
          };
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);

          return of({ token, role: normalizedRole });
        }
        return of({ token, role: '' });
      }),
      catchError((error) => this.handleError(error, 'Login failed'))
    );
  }

  // fetchEmployeeId(): Observable<any> {
  //   return this.http.get(`${this.apiUrl}/employees/all`, {
  //     headers: this.getAuthHeaders()
  //   }).pipe(
  //     map((res: any) => {
  //       if (this.isBrowser) {
  //         // ✅ Use correct key from backend
  //         localStorage.setItem('employeeId', res?.empId || '');
  //       }
  //       return res;
  //     }),
  //     catchError((err) => this.handleError(err, 'Fetching employee profile failed'))
  //   );
  // }

  fetchEmployeeById(empId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/employees/get/${empId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((res: any) => {
        if (this.isBrowser) {
          localStorage.setItem('employeeId', res?.empId?.toString() || '');
        }
        return res;
      }),
      catchError((err) => this.handleError(err, 'Fetching employee profile failed'))
    );
  }



  getEmployeeId(): string {
    const token = localStorage.getItem('token');
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.empId || payload.employeeId || '';
    } catch {
      return '';
    }
  }
  

  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/users/forgotPwd`, { email }, {
      responseType: 'text'
    }).pipe(
      map((res) => res),
      catchError((error) => this.handleError(error, 'Failed to send OTP'))
    );
  }

  resetPassword(email: string, otp: string, newPassword: string): Observable<any> {
    return this.http.patch(`${this.apiUrl}/users/resetPwd`, null, {
      params: { email, otp, newPassword },
      responseType: 'text'
    }).pipe(
      map((res) => res),
      catchError((error) => this.handleError(error, 'Password reset failed'))
    );
  }

  // =============================
  // ✅ REGISTRATION
  // =============================

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/users/create`, user, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    }).pipe(
      catchError((error) => this.handleError(error, 'Registration failed'))
    );
  }




  // =============================
  // ✅ EMPLOYEE METHODS
  // =============================

  createEmployeeFormData(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/employees/create`, formData, {
      headers: this.getMultipartAuthHeaders()
    });
  }


  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.apiUrl}/employees/all`, {
      headers: this.getAuthHeaders(),
      withCredentials: true
    });
  }

  getManagersByEmployee(): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.apiUrl}/employees/by-role/manager`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Failed to load managers'))
    );
  }


  // =============================
  // ✅ DEPARTMENT METHODS
  // =============================

  getAllDepartments(): Observable<Department[]> {
    return this.http.get<Department[]>(`${this.apiUrl}/departments/all`, {
      headers: this.getAuthHeaders(),
    });
  }

  getDepartmentById(id: number): Observable<Department> {
    return this.http.get<Department>(`${this.apiUrl}/departments/${id}`, {
      headers: this.getAuthHeaders(),
    });
  }

  createDepartment(dept: Department): Observable<Department> {
    return this.http.post<Department>(`${this.apiUrl}/departments/create`, dept, {
      headers: this.getAuthHeaders(),
    });
  }

  updateDepartment(id: number, dept: Department): Observable<Department> {
    return this.http.put<Department>(`${this.apiUrl}/departments/update/${id}`, dept, {
      headers: this.getAuthHeaders(),
    });
  }

  deleteDepartment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/departments/delete/${id}`, {
      headers: this.getAuthHeaders(),
    });
  }

  // =============================
  // ✅ USER METHODS
  // =============================

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users/all`, {
      headers: this.getAuthHeaders(),
    });
  }

  getAllUsersEmployee(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users/allEmp`, {
      headers: this.getAuthHeaders(),
    });
  }

  getUsersByRole(role: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users/role/${role}`, {
      headers: this.getAuthHeaders()
    });
  }
  // getManagersByDepartment(departmentId: number): Observable<User[]> {
  //   return this.http.get<User[]>(`/api/user/managers/by-department/${departmentId}`);
  // }

  getManagersByDepartment(departmentId: number): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/user/managers/by-department/${departmentId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Failed to load managers by department'))
    );
  }


  getUserId(): string {
    return this.isBrowser ? localStorage.getItem('employeeId') || '' : '';
  }

  hasRole(role: string): boolean {
    const roles = JSON.parse(localStorage.getItem('roles') || '[]');
    return roles.includes(role);
  }

  // =============================
  // ✅ ATTENDANCE METHODS
  // =============================

  // ========== ✅ Leave Type Methods ==========
  addLeaveType(leaveType: LeaveType): Observable<any> {
    return this.http.post(`${this.apiUrl}/leave-types/create`, leaveType, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Adding leave type failed'))
    );
  }

  updateLeaveType(id: string, leaveType: LeaveType): Observable<any> {
    return this.http.put(`${this.apiUrl}/leave-types/update/${id}`, leaveType, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Updating leave type failed'))
    );
  }

  getLeaveTypeById(id: string): Observable<LeaveType> {
    return this.http.get<LeaveType>(`${this.apiUrl}/leave-types/get/${id}`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching leave type failed'))
    );
  }

  // ✅ Fetch All Leave Types (matches your curl example)
getAllLeaveTypes(): Observable<LeaveType[]> {
  return this.http.get<LeaveType[]>(`${this.apiUrl}/leave-types/all`, {
    headers: this.getAuthHeaders(),
  }).pipe(
    catchError((error) => this.handleError(error, 'Fetching all leave types failed'))
  );
}


  deleteLeaveType(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/leave-types/delete/${id}`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Deleting leave type failed'))
    );
  }

  // ========== ✅ SALARY STRUCTURE METHODS ==========

  assignSalaryStructure(employeeId: string, payload: any): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/salary-structures/create/${employeeId}`,
      payload,
      { headers: this.getAuthHeaders() }
    );
  }

  getSalaryStructure(employeeId: string): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/salary-structures/employee/current/${employeeId}`,
      { headers: this.getAuthHeaders() }
    );
  }


  updateSalaryStructure(employeeId: string, payload: any): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/salary-structures/create/${employeeId}`,
      payload,
      { headers: this.getAuthHeaders() }
    );
  }


  deleteSalaryStructure(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/salary-structures/delete/${id}`, {
      headers: this.getAuthHeaders(),
    });
  }

  // ========== ✅ PAYROLL METHODS ==========


  generatePayroll(empId: number, month: string, year: number): Observable<Payroll> {
    const params = new URLSearchParams();
    params.set('empId', empId.toString());
    params.set('month', month)
    params.set('year', year.toString());

    return this.http.post<Payroll>(
      `${this.apiUrl}/payroll/generate?${params.toString()}`,
      null,
      { headers: this.getAuthHeaders() }
    );
  }

  downloadPayslip(empId: number, month: string, year: number): Observable<HttpResponse<Blob>> {
    const params = new URLSearchParams();
    params.set('empId', empId.toString());
    params.set('month', month);
    params.set('year', year.toString());

    return this.http.get(`${this.apiUrl}/payroll/download?${params.toString()}`, {
      headers: this.getAuthHeaders(),
      responseType: 'blob',
      observe: 'response'
    });
  }


  viewPayroll(empId: number, month: string, year: number): Observable<Payroll> {
    const params = new URLSearchParams();
    params.set('empId', empId.toString());
    params.set('month', month);
    params.set('year', year.toString());

    return this.http.get<Payroll>(
      `${this.apiUrl}/payroll/view?${params.toString()}`,
      {
        headers: this.getAuthHeaders()
      }
    );
  }


  //  // ✅ Fetch Salary Structure by EmpId
  // fetchSalaryStructure(empId: string): Observable<any> {
  //   return this.http.get(`${this.apiUrl}/salaryStructure/${empId}`);
  // }

  //   addPayroll(payload: any): Observable<any> {
  //     return this.http.post(`${this.apiUrl}/addPayroll`, payload);
  //   }

  //   generatePayslip(empId: string, month: string): Observable<any> {
  //     const params = new HttpParams().set('month', month);
  //     return this.http.patch(`${this.apiUrl}/generatePayslip/${empId}`, {}, { params });
  //   }

  //   downloadPayslipByAdmin(empId: string, month: string): Observable<Blob> {
  //     const params = new HttpParams().set('month', month);
  //     return this.http.get(`${this.apiUrl}/downloadPayslip/${empId}`, { params, responseType: 'blob' });
  //   }

  //   downloadPayslipByEmployee(month: string): Observable<Blob> {
  //     const params = new HttpParams().set('month', month);
  //     return this.http.get(`${this.apiUrl}/downloadPayslip`, { params, responseType: 'blob' });
  //   }

  //   viewPayslipByAdmin(empId: string, month: string): Observable<any> {
  //     const params = new HttpParams().set('month', month);
  //     return this.http.get(`${this.apiUrl}/viewPayslip/${empId}`, { params });
  //   }

  // viewPayslipByEmployee(month: string): Observable<any> {
  //     const params = new HttpParams().set('month', month);
  //     return this.http.get(`${this.apiUrl}/viewPayslip`, { params });
  // }

  // getSalaryStructureByEmployeeId(empId: string): Observable<any> {
  //   return this.http.get(`${this.apiUrl}/salaryStructure/get/by-employee/${empId}`);
  // }

  getRole(): string {
    const token = localStorage.getItem('token');
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role || payload.Role || '';
    } catch {
      return '';
    }
  }

  getUserRole(): string {
    // Prefer the cached current user value
    const userRole = this.currentUserValue?.role;
    if (userRole) return userRole;

    // Fallback: decode from JWT token if available
    const token = localStorage.getItem('token');
    if (!token) return '';

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return (payload.role || payload.Role || '').replace('ROLE_', '').toUpperCase();
    } catch {
      return '';
    }
  }
  // *******************BANKING METHOD*****************

  // ✅ Add Bank Details (FormData if with file upload, or JSON payload)
  createBankDetails(emplId: string, payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/banking/create/${emplId}`, payload, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Adding bank details failed'))
    );
  }

  // ✅ Fetch All Bank Details
  getAllBankDetails(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/banking/all`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching bank details failed'))
    );
  }

  // ✅ Fetch Bank Details by Employee Id
  getBankDetailsByEmployeeId(empId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/banking/by-employee/${empId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching bank details failed'))
    );
  }

  // ✅ Update Bank Details
  updateBankDetailsFormData(bankDetailId: string, formData: FormData): Observable<any> {
    return this.http.put(`${this.apiUrl}/banking/update/${bankDetailId}`, formData, {
      headers: this.getMultipartAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Updating bank details failed'))
    );
  }

  // ✅ Delete Bank Details
  deleteBankDetails(bankDetailsId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/banking/delete/${bankDetailsId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Deleting bank details failed'))
    );
  }

  // getEmployeeIdFromToken(): string {
  //   const token = localStorage.getItem('token');
  //   if (!token) {  console.error('No token found');
  //     return '';
  //   }
  //   try {
  //     const payload = JSON.parse(atob(token.split('.')[1]));
  //     console.log('Token payload:', payload);
  //     const empId = payload.empId || payload.employeeId || payload.id; 

  //       if (!empId) {
  //       console.error('No employee ID found in token payload');
  //     }
  //      return empId || '';

  //   } catch (e) {
  //     console.error('Failed to parse token payload:', e);
  //     return '';
  //   }
  // }
  getEmployeeIdFromToken(): string {
    const token = localStorage.getItem('token');
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.empId || payload.employeeId || '';
    } catch (e) {
      console.error('Failed to parse token payload:', e);
      return '';
    }
  }

  // getEmployeeIdFromToken(): number {
  //   const token = localStorage.getItem('token');
  //   if (!token) {
  //     console.error('No token found');
  //     return 0;
  //   }
  //   try {
  //     const payload = JSON.parse(atob(token.split('.')[1]));
  //     console.log('Token payload:', payload);

  //     const empId = payload.empId ?? payload.employeeId ?? payload.id;
  //     if (empId == null) {
  //       console.error('No employee ID found in token payload');
  //       return 0;
  //     }

  //     return Number(empId); // ensure numeric type
  //   } catch (e) {
  //     console.error('Failed to parse token payload:', e);
  //     return 0;
  //   }
  // }

  //  *******************Benefits METHOD*****************
  addBenefit(benefit: Benefit): Observable<any> {
    return this.http.post(`${this.apiUrl}/benefits/create`, benefit, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Adding Benefits failed'))
    );
  }

  updateBenefit(id: string, benefit: Benefit): Observable<any> {
    return this.http.put(`${this.apiUrl}/benefits/update/${id}`, benefit, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Updating benefit failed'))
    );
  }

  getBenefitById(id: string): Observable<Benefit> {
    return this.http.get<Benefit>(`${this.apiUrl}/benefits/get/${id}`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching benefit failed'))
    );
  }

  getAllBenefits(): Observable<Benefit[]> {
    return this.http.get<Benefit[]>(`${this.apiUrl}/benefits/all`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching all benefits failed'))
    );
  }

  deleteBenefits(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/benefits/delete/${id}`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Deleting benefits failed'))
    );
  }


  // *******************PROJECTS METHOD*****************
  addProject(project: Project): Observable<any> {
    return this.http.post(`${this.apiUrl}/projects/create`, project, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Adding project failed'))
    );
  }

  updateProject(id: string, project: Project): Observable<any> {
    return this.http.put(`${this.apiUrl}/projects/update/${id}`, project, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Updating project failed'))
    );
  }

  getProjectById(id: string): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/projects/get/${id}`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching project failed'))
    );
  }

  getAllProject(): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.apiUrl}/projects/all`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching all project failed'))
    );
  }

  //  *******************Complaince METHOD*****************
  addComplaince(compliance: Compliance): Observable<any> {
    return this.http.post(`${this.apiUrl}/compliances/create`, compliance, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Adding compliance failed'))
    );
  }

  updateCompliance(id: string, compliance: Compliance): Observable<any> {
    return this.http.put(`${this.apiUrl}/compliances/update/${id}`, compliance, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Updating complaince failed'))
    );
  }

  getComplianceById(id: string): Observable<Compliance> {
    return this.http.get<Compliance>(`${this.apiUrl}/compliances/get/${id}`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching compliance failed'))
    );
  }

  getAllCompliance(): Observable<Compliance[]> {
    return this.http.get<Compliance[]>(`${this.apiUrl}/compliances/all`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching all compliance failed'))
    );
  }

  deleteComplaince(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/compliances/delete/${id}`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Deleting compliance failed'))
    );
  }
  //*****************************************ATTENDANCE************************* */

  // ✅ Clock-In
  clockInAttendance(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/attendance/clock-in`, payload, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    }).pipe(
      catchError(error => this.handleError(error, 'Clock-in failed'))
    );
  }

  // ✅ Clock-Out
  clockOutAttendance(): Observable<any> {
    return this.http.put(`${this.apiUrl}/attendance/clock-out`, {}, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    }).pipe(
      catchError(error => this.handleError(error, 'Clock-out failed'))
    );
  }


  // ✅ Submit Regularize Request
  requestRegularization(date: string, reason: string): Observable<any> {
    const params = new HttpParams()
      .set('date', date)
      .set('reason', reason);

    return this.http.post(`${this.apiUrl}/attendance/regularize/request`, null, {
      headers: this.getAuthHeaders(),
      responseType: 'text',
      params
    }).pipe(
      catchError(error => this.handleError(error, 'Regularize request failed'))
    );
  }

//Approve Regularization request
  approveRegularization(attendanceId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/attendance/regularize/approve/${attendanceId}`, null, {
      headers: this.getAuthHeaders(),
       responseType: 'text' as 'json'
    });
  }

  // ✅ Reject Regularize Request
  rejectRegularizationRequest(attendanceId: number, reason: string): Observable<any> {
    const params = new HttpParams().set('rejectionReason', reason);
    return this.http.put(`${this.apiUrl}/attendance/regularize/reject/${attendanceId}`, null, {
      headers: this.getAuthHeaders(),
      responseType: 'text' as 'json',
      params
    }).pipe(
      catchError(error => this.handleError(error, 'Reject request failed'))
    );
  }

  // ✅ Download CSV Report
  downloadAttendanceCsvReport(empId: string, fromDate: string, toDate: string): Observable<Blob> {
    const params = new HttpParams()
      .set('empId', empId)
      .set('fromDate', fromDate)
      .set('toDate', toDate);
    return this.http.get(`${this.apiUrl}/attendance/report/csv`, {
      headers: this.getAuthHeaders(),
      params,
      responseType: 'blob'
    });
  }

  // ✅ Download PDF Report
  downloadAttendancePdfReport(empId: string, department: string, fromDate: string, toDate: string): Observable<Blob> {
    const params = new HttpParams()
      .set('empId', empId)
      .set('department', department)
      .set('fromDate', fromDate)
      .set('toDate', toDate);
    return this.http.get(`${this.apiUrl}/attendance/report/pdf`, {
      headers: this.getAuthHeaders(),
      params,
      responseType: 'blob'
    });
  }

  // ✅ Monthly Attendance Status
  getMonthlyAttendanceStatus(empId: string, month: number, year: number): Observable<any> {
    const params = new HttpParams()
      .set('month', month.toString())
      .set('year', year.toString());
    return this.http.get(`${this.apiUrl}/attendance/status/${empId}`, {
      headers: this.getAuthHeaders(),
      params
    });
  }
  // get All regularization Requests
  getAllRegularizationRequests(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/attendance/regularize/requests/all`, {
      headers: this.getAuthHeaders()
    });
  }
  // ✅ Get Current Attendance Status
getCurrentAttendanceStatus(): Observable<any> {
  return this.http.get(`${this.apiUrl}/attendance/current-status`, {
    headers: this.getAuthHeaders()
  });
}


}
