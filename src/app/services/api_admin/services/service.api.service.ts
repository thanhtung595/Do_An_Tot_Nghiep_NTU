import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class ServiceApiService {

  private apiUrl = `${API_BASE_URL}api/services`;

  constructor(private http: HttpClient) { }

  // Hàm lấy danh sách service
  getAllServices(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  // Hàm create service
  createService(service: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, service);
  }

  // Hàm update service
  updateService(service: any): Observable<any> {
    return this.http.put<any>(this.apiUrl, service);
  }

  // Hàm delete service
  deleteService(id: any): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}
