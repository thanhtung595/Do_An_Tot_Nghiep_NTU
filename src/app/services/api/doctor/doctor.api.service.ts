import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class DoctorApiService {

  private apiUrl = `${API_BASE_URL}api/appointment`;

  constructor(private http: HttpClient) { }

  // Hàm lấy danh sách header
  getPatientHistory(): Observable<any> {
    return this.http.get<any>(this.apiUrl + "/patient-history");
  }

  updateStatusAppointment(data: any): Observable<any> {
    return this.http.put<any>(this.apiUrl +"/update-status-appointment", data);
  }
}
