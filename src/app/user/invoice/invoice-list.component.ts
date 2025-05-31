import { Component, OnInit } from '@angular/core';
import { Invoice } from '@app/model/invoice.model';

@Component({
  selector: 'app-invoice-list',
  templateUrl: './invoice-list.component.html',
  styleUrls: ['./invoice-list.component.scss']
})
export class InvoiceListComponent implements OnInit {
  invoices: Invoice[] = [];
  selectedInvoice: Invoice | null = null;
  showInvoiceDetail = false;

  constructor() { }

  ngOnInit(): void {
    this.generateMockData();
  }

  generateMockData(): void {
    this.invoices = [
      {
        id: 1,
        patientName: 'Nguyễn Văn A',
        age: 35,
        gender: 'Nam',
        address: '123 Đường ABC, Quận 1, TP.HCM',
        phone: '0123456789',
        symptoms: 'Sốt, ho, đau họng',
        diagnosis: 'Viêm họng cấp',
        treatment: 'Điều trị nội khoa',
        medications: ['Paracetamol 500mg', 'Amoxicillin 500mg'],
        notes: 'Nghỉ ngơi nhiều, uống nhiều nước',
        nextAppointment: '2024-03-20',
        totalMoney: 500000,
        isPaid: false,
        date: new Date('2024-03-15')
      },
      {
        id: 2,
        patientName: 'Trần Thị B',
        age: 28,
        gender: 'Nữ',
        address: '456 Đường XYZ, Quận 2, TP.HCM',
        phone: '0987654321',
        symptoms: 'Đau đầu, mệt mỏi',
        diagnosis: 'Thiếu máu',
        treatment: 'Bổ sung sắt',
        medications: ['Ferrovit', 'Vitamin B12'],
        notes: 'Tái khám sau 1 tháng',
        nextAppointment: '2024-04-15',
        totalMoney: 750000,
        isPaid: true,
        date: new Date('2024-03-14')
      }
    ];
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
    invoice.isPaid = true;
    this.closeInvoiceDetail();
  }

  printInvoice(invoice: Invoice): void {
    window.print();
  }
}
