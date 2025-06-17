import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'


@Injectable({
  providedIn: 'root'
})
export class AuthApiService {

  private apiUrl = `${API_BASE_URL}api/auth/login`;
  private apiUrlRequiredRole = `${API_BASE_URL}api/auth/`;

  constructor(private http: HttpClient) { }

  // Hàm login
  login(username: string, password: string): Observable<any> {
    const body = { username, password };
    return this.http.post<any>(this.apiUrl, body);
  }

  // Hàm register
  register(user: any): Observable<any> {
    return this.http.post<any>(API_BASE_URL+"api/auth/register", user);
  }

  // Hàm logout
  logout(): Observable<any> {
    return this.http.get<any>(API_BASE_URL+"api/auth/logout/", { withCredentials: true });
  }

  requiredRoleAdmin(): Observable<any> {
    console.log(this.apiUrlRequiredRole+"required-admin")
    return this.http.get<any>(this.apiUrlRequiredRole+"required-admin");
  }

  requiredRoleDoctor(): Observable<any> {
    return this.http.get<any>(this.apiUrlRequiredRole+"required-doctor");
  }

  requiredRolePatient(): Observable<any> {
    return this.http.get<any>(this.apiUrlRequiredRole+"required-patient");
  }
}
