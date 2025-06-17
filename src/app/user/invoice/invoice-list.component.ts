import { Component, OnInit } from '@angular/core';
import { Invoice } from '@app/model/invoice.model';
import { ToastService } from '@app/services/toast/toast.service';
import { InvoiceAPIService } from '@app/services/api/invoice/invoice.api.service'
import { Router } from '@angular/router';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { clearAccessToken } from '@app/services/token/TokenService';

@Component({
  selector: 'app-invoice-list',
  templateUrl: './invoice-list.component.html',
  styleUrls: ['./invoice-list.component.scss']
})
export class InvoiceListComponent implements OnInit {
  invoices: Invoice[] = [];
  selectedInvoice: Invoice | null = null;
  showInvoiceDetail = false;

  constructor(private toastService: ToastService,
    private invoiceAPIService: InvoiceAPIService,
    private router: Router,
    private authService: AuthApiService,
  ) {}

  ngOnInit(): void {
    this.requiredRole();
    this.generateMockData();
  }

  generateMockData(): void {
    this.invoiceAPIService.getInvoice().subscribe({
      next: (data) => {
        this.invoices = data?.data.invoices ?? [];
        this.getListNameMedicinesInvoiceByIdPatient();
        this.getListNameServiceInvoiceByIdPatient();
        console.log("invoices",this.invoices);
      },
      error: (error) => {
        console.error('Error fetching invoices:', error);
      }
    });
  }

  getListNameMedicinesInvoiceByIdPatient() {
    this.invoices.forEach((invoice) => {
      this.invoiceAPIService.getListNameMedicinesInvoiceByIdPatient(invoice.id).subscribe({
        next: (res: any) => {
          console.log(res);
          invoice.medications = res?.data?.medicines ?? [];
        },
        error: (error: any) => {
          console.error('Error fetching medications:', error);
        }
      });
    });
  }

  getListNameServiceInvoiceByIdPatient() {
    this.invoices.forEach((invoice) => {
      this.invoiceAPIService.getListNameServiceInvoiceByIdPatient(invoice.patientid).subscribe({
        next: (res: any) => {
          console.log(res);
          invoice.services = res?.data?.services ?? [];
        },
        error: (error: any) => {
          console.error('Error fetching services:', error);
        }
      });
    });
  }

  viewInvoiceDetail(invoice: Invoice): void {
    this.selectedInvoice = invoice;
    this.showInvoiceDetail = true;
  }

  closeInvoiceDetail(): void {
    this.showInvoiceDetail = false;
    this.selectedInvoice = null;
  }

  payInvoice(invoice: Invoice): void {
    const recordId = invoice.id;

    if (recordId) {
      console.log('Navigating to payment with ID:', recordId); // Debug log
      this.router.navigate(['/payment', recordId]).then(
        (success) => {
          if (!success) {
            console.error('Navigation failed');
            alert('Không thể chuyển đến trang thanh toán. Vui lòng thử lại sau.');
          }
        }
      );
    } else {
      console.error('Record ID is undefined');
      alert('Không thể thực hiện thanh toán. Vui lòng thử lại sau.');
    }

    // invoice.isPaid = true;
    // this.toastService.success('Thanh toán thành công!');
    // this.closeInvoiceDetail();
  }

  printInvoice(invoice: Invoice): void {
    window.print();
  }

  navigateToPayment(recordId: any): void {

  }

  requiredRole(){
    this.authService.requiredRolePatient().subscribe({
      next: (response) => {
        return;
      },
      error: (error) => {
        clearAccessToken();
        window.location.href = '/login';
      }
    });
  }
}
