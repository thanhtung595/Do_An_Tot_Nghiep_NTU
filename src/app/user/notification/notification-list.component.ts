import { Component, OnInit } from '@angular/core';
import { Notification } from '@app/model/notification.model';
import { NotificationApiService } from '@app/services/api/notification/notification.api.service';

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

  constructor(private notificationApiService : NotificationApiService) { }

  ngOnInit(): void {
    // this.generateMockData();
    this.getAllNotification();
  }

  viewNotificationDetail(notification: Notification): void {
    this.selectedNotification = notification;
    this.showNotificationDetail = true;
    if (!notification.isread) {

      this.notificationApiService.updateIsRead(notification).subscribe({
      next: (data) => {
        notification.isread = true;
      },
      error: (error) => {
        console.error('Error fetching bookAppointment:', error);
      }
    });
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
        notification.patientname.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
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

  getAllNotification(): void {
    this.notificationApiService.getNotification().subscribe({
      next: (data) => {
        this.notifications = (data.data.notification ?? []).sort(
          (a: Notification, b: Notification) =>
            new Date(b.date).getTime() - new Date(a.date).getTime()
        );
        // console.log("getNotification loaded:", data.data.notification);
      },
      error: (error) => {
        console.error('Error loading headers:', error);
      }
    });
  }
}
