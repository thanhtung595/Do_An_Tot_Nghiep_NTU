import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

interface PaymentItem {
  name: string;
  quantity: number;
  price: number;
}

interface PaymentInfo {
  id: number;
  patientName: string;
  date: string;
  diagnosis: string;
  amount: number;
  items: PaymentItem[];
}

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  paymentInfo: PaymentInfo = {
    id: 0,
    patientName: '',
    date: '',
    diagnosis: '',
    amount: 0,
    items: []
  };

  selectedPaymentMethod: string = '';
  isProcessing: boolean = false;

  // Dữ liệu mẫu cho các phương thức thanh toán
  paymentMethods = [
    {
      id: 'momo',
      name: 'Ví MoMo',
      icon: 'assets/images/momo.png',
      description: 'Thanh toán qua ví MoMo'
    },
    {
      id: 'vnpay',
      name: 'VNPay',
      icon: 'assets/images/vnpay.png',
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
    private router: Router
  ) {}

  ngOnInit(): void {
    // Lấy ID từ route params
    this.route.params.subscribe(params => {
      const recordId = params['id'];
      // TODO: Gọi API để lấy thông tin thanh toán
      this.loadMockData(recordId);
    });
  }

  // Hàm tạo dữ liệu mẫu
  private loadMockData(recordId: number): void {
    this.paymentInfo = {
      id: recordId,
      patientName: 'Nguyễn Văn A',
      date: '2024-03-20',
      diagnosis: 'Viêm họng cấp',
      amount: 1500000,
      items: [
        {
          name: 'Khám bệnh',
          quantity: 1,
          price: 500000
        },
        {
          name: 'Thuốc kháng sinh',
          quantity: 2,
          price: 300000
        },
        {
          name: 'Xét nghiệm máu',
          quantity: 1,
          price: 400000
        }
      ]
    };
  }

  // Tính tổng tiền
  getTotal(): number {
    return this.paymentInfo.items.reduce((total, item) => {
      return total + (item.price * item.quantity);
    }, 0);
  }

  // Xử lý thanh toán
  processPayment(): void {
    if (!this.selectedPaymentMethod) {
      alert('Vui lòng chọn phương thức thanh toán');
      return;
    }

    this.isProcessing = true;

    // Giả lập quá trình thanh toán
    setTimeout(() => {
      this.isProcessing = false;
      // TODO: Gọi API thanh toán thực tế
      alert('Thanh toán thành công!');
      this.router.navigate(['/payment-success']);
    }, 2000);
  }

  // Hủy thanh toán
  cancel(): void {
    this.router.navigate(['/profile']);
  }
} 