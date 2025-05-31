import { Component, OnInit } from '@angular/core';
import { Notification } from '@app/model/notification.model';

@Component({
  selector: 'app-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.scss']
})
export class NotificationListComponent implements OnInit {
  notifications: Notification[] = [];
  selectedNotification: Notification | null = null;
  showNotificationDetail = false;
  filterType: string = 'all';
  searchTerm: string = '';

  constructor() { }

  ngOnInit(): void {
    this.generateMockData();
  }

  generateMockData(): void {
    this.notifications = [
      {
        id: 1,
        patientName: 'Bệnh nhân 1',
        phone: '0123456789',
        type: 'appointment',
        title: 'Lịch hẹn khám bệnh mới',
        content: 'Bạn có lịch hẹn khám bệnh vào ngày 20/03/2024 lúc 14:00. Vui lòng đến đúng giờ.',
        date: new Date('2024-03-15'),
        isRead: false,
        status: 'sent',
        appointmentDate: new Date('2024-03-20'),
        appointmentTime: '14:00'
      },
      {
        id: 2,
        patientName: 'Bệnh nhân 1',
        phone: '0987654321',
        type: 'reminder',
        title: 'Nhắc nhở uống thuốc',
        content: 'Nhắc nhở: Bạn cần uống thuốc Ferrovit sau bữa ăn sáng.',
        date: new Date('2024-03-15'),
        isRead: true,
        status: 'sent'
      },
      {
        id: 3,
        patientName: 'Bệnh nhân 1',
        phone: '0912345678',
        type: 'result',
        title: 'Kết quả xét nghiệm',
        content: 'Kết quả xét nghiệm của bạn đã có. Vui lòng đến phòng khám để nhận kết quả.',
        date: new Date('2024-03-14'),
        isRead: false,
        status: 'sent'
      },
      {
        id: 4,
        patientName: 'Bệnh nhân 1',
        phone: '0978123456',
        type: 'payment',
        title: 'Nhắc nhở thanh toán',
        content: 'Bạn còn một khoản phí khám bệnh chưa thanh toán. Vui lòng thanh toán sớm.',
        date: new Date('2024-03-14'),
        isRead: false,
        status: 'pending'
      }
    ];
  }

  viewNotificationDetail(notification: Notification): void {
    this.selectedNotification = notification;
    this.showNotificationDetail = true;
    if (!notification.isRead) {
      notification.isRead = true;
    }
  }

  closeNotificationDetail(): void {
    this.showNotificationDetail = false;
    this.selectedNotification = null;
  }

  getFilteredNotifications(): Notification[] {
    return this.notifications.filter(notification => {
      const matchesType = this.filterType === 'all' || notification.type === this.filterType;
      const matchesSearch = this.searchTerm === '' ||
        notification.patientName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        notification.title.toLowerCase().includes(this.searchTerm.toLowerCase());
      return matchesType && matchesSearch;
    });
  }

  getTypeLabel(type: string): string {
    switch(type) {
      case 'appointment': return 'Lịch hẹn';
      case 'reminder': return 'Nhắc nhở';
      case 'result': return 'Kết quả';
      case 'payment': return 'Thanh toán';
      default: return type;
    }
  }

  getStatusLabel(status: string): string {
    switch(status) {
      case 'pending': return 'Chờ gửi';
      case 'sent': return 'Đã gửi';
      case 'failed': return 'Gửi thất bại';
      default: return status;
    }
  }

  resendNotification(notification: Notification): void {
    notification.status = 'sent';
    this.closeNotificationDetail();
  }
}
