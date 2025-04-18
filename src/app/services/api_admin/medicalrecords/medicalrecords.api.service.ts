import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class MedicalrecordsApiService {

  constructor(private http: HttpClient) { }

  getAllAppointment(): Observable<any> {
    return this.http.get<any>(API_BASE_URL+"api/appointment-getall/");
  }

  // Hàm update doctor
  updateAppointment(appointment: any): Observable<any> {
    return this.http.post<any>(API_BASE_URL+"/api/appointment-update/", appointment, { withCredentials: true });
  }
}
