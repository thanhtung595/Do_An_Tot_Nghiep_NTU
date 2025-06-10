import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class DoctorApiService {

  private apiUrl = `${API_BASE_URL}api/appointment`;
  private apiUrlServices = `${API_BASE_URL}api/services`;
  private apiUrlMedicines = `${API_BASE_URL}api/medicines`;
  private apiUrlAppointmentRecord = `${API_BASE_URL}api/appointment/medical-record`;

  constructor(private http: HttpClient) { }

  // Hàm lấy danh sách header
  getPatientHistory(): Observable<any> {
    return this.http.get<any>(this.apiUrl + "/patient-history");
  }

  updateStatusAppointment(data: any): Observable<any> {
    return this.http.put<any>(this.apiUrl +"/update-status-appointment", data);
  }

  saveMedicalRecord(data: any): Observable<any> {
    return this.http.put<any>(this.apiUrl +"/save-medical-record", data);
  }

  // Hàm lấy danh sách service
  getService(): Observable<any> {
    return this.http.get<any>(this.apiUrlServices);
  }

  // Hàm lấy danh sách Medicines
  getMedicines(): Observable<any> {
    return this.http.get<any>(this.apiUrlMedicines);
  }

  // Hàm lấy danh sách AppointmentRecord
  getAppointmentRecordById(id: number): Observable<any> {
    const url = `${this.apiUrlAppointmentRecord}/${id}`;
    console.log('id', id);
    console.log('url', url);
    return this.http.get<any>(url);
  }
}
