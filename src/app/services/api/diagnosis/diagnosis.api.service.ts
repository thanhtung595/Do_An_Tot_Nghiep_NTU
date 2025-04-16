import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class DiagnosisApiService {

  private apiUrl = `${API_BASE_URL}api/diagnose-ai/`;

  constructor(private http: HttpClient) { }

  getDiagnosiAI(symptoms: string): Observable<any> {
    return this.http.post<any>(this.apiUrl, { symptoms }, { withCredentials: true });
  }
}
