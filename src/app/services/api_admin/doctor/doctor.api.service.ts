import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class DoctorApiService {

  private apiUrl = `${API_BASE_URL}api/departments/`;

  constructor(private http: HttpClient) { }

  // Hàm lấy danh sách service
  getAllDepartments(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  // Hàm create doctor
  createDoctor(doctor: any): Observable<any> {
    return this.http.post<any>(API_BASE_URL+"api/doctor/", doctor, { withCredentials: true });
  }

  // Hàm update doctor
  updateDoctor(doctor: any): Observable<any> {
    return this.http.put<any>(API_BASE_URL+"api/doctor/", doctor, { withCredentials: true });
  }

  // Hàm get all doctor
  getAllDoctor(): Observable<any> {
    return this.http.get<any>(API_BASE_URL+"api/doctor/");
  }

  // Hàm đặt lịch khám
  createAppointments(doctor: any): Observable<any> {
    return this.http.post<any>(API_BASE_URL+"api/doctor/appointment/", doctor, { withCredentials: true });
  }
}
