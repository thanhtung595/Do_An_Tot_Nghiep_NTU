import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '@app/constants'

@Injectable({
  providedIn: 'root'
})
export class InvoiceAPIService {

  private apiUrl = `${API_BASE_URL}api/invoice`;
  private apiUrlPayment = `${API_BASE_URL}api/payment`;

  constructor(private http: HttpClient) { }

  // Hàm lấy danh sách invoice
  getInvoice(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  getListNameMedicinesInvoiceByIdPatient(id: number): Observable<any> {
    const url = `${API_BASE_URL}api/medicines/medicines-patientid/${id}`;
    return this.http.get<any>(url);
  }

  getListNameServiceInvoiceByIdPatient(id: number): Observable<any> {
    const url = `${API_BASE_URL}api/services/service-patientid/${id}`;
    // console.log('url: ', url);
    // console.log('id: ', id);
    return this.http.get<any>(url);
  }

  getInvoiceByIdUserAndInvoice(id: number): Observable<any> {
    const url = `${this.apiUrl}/by-id/${id}`;
    return this.http.get<any>(url);
  }

  createPayment(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrlPayment, data);
  }
}
