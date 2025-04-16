import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class ProfleApiServiceService {

  private apiUrl = `${API_BASE_URL}api/user/`;

  constructor(private http: HttpClient) { }

  // Hàm lấy user
  getUser(): Observable<any> {
    return this.http.get<any>(this.apiUrl, { withCredentials: true });
  }

  // Hàm lấy Appointment
  getAppointment(): Observable<any> {
    return this.http.get<any>(API_BASE_URL+"api/appointment/", { withCredentials: true });
  }
}
