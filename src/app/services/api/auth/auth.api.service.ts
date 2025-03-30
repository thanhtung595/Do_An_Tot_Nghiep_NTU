import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'


@Injectable({
  providedIn: 'root'
})
export class AuthApiService {

  // private apiUrl = `${API_BASE_URL}api/login/`;
  private apiUrl = "http://localhost/auth/login";

  constructor(private http: HttpClient) { }

  // Hàm login
  login(userName: string, password: string): Observable<any> {
    const body = { userName, password };
    return this.http.post<any>(this.apiUrl, body, { withCredentials: true });
  }

  // Hàm register
  register(user: any): Observable<any> {
    return this.http.post<any>(API_BASE_URL+"api/register/", user, { withCredentials: true });
  }

  // Hàm logout
  logout(): Observable<any> {
    return this.http.get<any>(API_BASE_URL+"api/logout/", { withCredentials: true });
  }
}
