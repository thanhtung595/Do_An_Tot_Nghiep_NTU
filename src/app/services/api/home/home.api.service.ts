import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class HomeApiService {

  private apiUrl = `${API_BASE_URL}api/services`;

  constructor(private http: HttpClient) { }

  // Hàm lấy danh sách service
  getService(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  // // Hàm gửi dữ liệu lên API (POST)
  // createUser(data: any): Observable<any> {
  //   return this.http.post<any>(this.apiUrl, data);
  // }
}
