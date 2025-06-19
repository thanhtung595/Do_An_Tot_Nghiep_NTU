import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class FeedbackApiService {

  private apiUrl = `${API_BASE_URL}api/feedback`;

  constructor(private http: HttpClient) { }

  getAllLimit(limit: number): Observable<any> {
    const url = `${this.apiUrl}/${limit}`;
    return this.http.get<any>(url);
  }

  createFeedback(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, data);
  }
}
