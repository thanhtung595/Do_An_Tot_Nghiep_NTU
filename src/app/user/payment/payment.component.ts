import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InvoiceAPIService } from '@app/services/api/invoice/invoice.api.service'
import { ToastService } from '@app/services/toast/toast.service';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { clearAccessToken } from '@app/services/token/TokenService';

interface PaymentItem {
  name: string;
  date: string,
  price: number;
}

interface PaymentInfo {
  id: number;
  patientname: string;
  date: string;
  diagnosis: string;
  amount: number;
  items: PaymentItem[];
  paymentmethod: string;
}

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  paymentInfo: PaymentInfo = {
    id: 0,
    patientname: '',
    date: '',
    diagnosis: '',
    amount: 0,
    paymentmethod : '',
    items: []
  };

  selectedPaymentMethod: string = '';
  isProcessing: boolean = false;

  // Dữ liệu mẫu cho các phương thức thanh toán
  paymentMethods = [
    {
      id: 'momo',
      name: 'Ví MoMo',
      icon: 'https://ntt-datn-clinic-management.s3.ap-southeast-1.amazonaws.com/img/payment/momo.png',
      description: 'Thanh toán qua ví MoMo'
    },
    {
      id: 'vnpay',
      name: 'VNPay',
      icon: 'https://ntt-datn-clinic-management.s3.ap-southeast-1.amazonaws.com/img/payment/vnpay.png',
      description: 'Thanh toán qua VNPay'
    },
    {
      id: 'cash',
      name: 'Tiền mặt',
      icon: 'fas fa-money-bill',
      description: 'Thanh toán trực tiếp tại bệnh viện'
    }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private invoiceAPIService: InvoiceAPIService,
    private toastService: ToastService,
    private authService: AuthApiService,
  ) {}

  ngOnInit(): void {
    this.requiredRole();
    // Lấy ID từ route params
    this.route.params.subscribe(params => {
      const recordId = params['id'];
      // TODO: Gọi API để lấy thông tin thanh toán
      this.loadMockData(recordId);
    });
  }

  // Hàm tạo dữ liệu mẫu
  private loadMockData(recordId: number): void {
    this.invoiceAPIService.getInvoiceByIdUserAndInvoice(recordId).subscribe({
      next: (data) => {
        this.paymentInfo = data?.data.invoices ?? [];
        this.paymentInfo.items = data?.data.service ?? [];
      },
      error: (error) => {
        console.error('Error fetching invoices:', error);
      }
    });
  }

  // Tính tổng tiền
  getTotal(): number {
    return this.paymentInfo.items.reduce((total, item) => {
      return total + (item.price);
    }, 0);
  }

  // Xử lý thanh toán
  processPayment(record : PaymentInfo): void {
    if (!this.selectedPaymentMethod) {
      this.toastService.warning('Vui lòng chọn phương thức thanh toán');
      return;
    }

    record.paymentmethod = this.selectedPaymentMethod;

    console.log(record);

    this.invoiceAPIService.createPayment(record).subscribe({
      next: (data) => {
        this.isProcessing = true;
        this.toastService.success('Thanh toán thành công.');
        this.router.navigate(['/invoice']);
      },
      error: (error) => {
        console.error('Error fetching createPayment:', error);
        this.toastService.warning(error.data.message);
      }
    });
  }

  // Hủy thanh toán
  cancel(): void {
    this.router.navigate(['/invoice']);
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
