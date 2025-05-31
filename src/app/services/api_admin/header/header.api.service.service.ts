import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class HeaderApiServiceService {

  private apiUrl = `${API_BASE_URL}api/header/admin`;

  constructor(private http: HttpClient) { }

  // Hàm lấy danh sách header
  getHeader(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }
}
