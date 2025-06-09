import { Component, OnInit } from '@angular/core';
import { Notification } from '@app/model/notification.model';
import { NotificationApiService } from '@app/services/api/notification/notification.api.service';
import { NotificationService } from '@app/services/notification/notification.service';

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

  // Pagination properties
  currentPage = 1;
  itemsPerPage = 5;
  totalPages = 1;

  constructor(private notificationApiService : NotificationApiService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    // this.generateMockData();
    this.getAllNotification();
    this.wsBodyNotifications();
  }

  wsBodyNotifications(){
    this.notificationService.connectionStatus$.subscribe(connected => {
      if (connected) {
        // Đợi đến khi kết nối mới subscribe topic
        this.notificationService.subscribeToTopic('/user/queue/body/notification').subscribe(data => {
          if (data) {
            this.getAllNotification();
          }
        });
      }
    });
  }

  ngOnDestroy() {
    // Ngắt kết nối WebSocket
    this.notificationService.unsubscribeFromTopic('/user/queue/body/notification');
    this.notificationService.disconnect();
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
    let filtered = this.notifications;

    // Filter by search term
    if (this.searchTerm) {
      filtered = filtered.filter(notification =>
        notification.title.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }

    // Filter by type and read status
    if (this.filterType !== 'all') {
      if (this.filterType === 'read') {
        filtered = filtered.filter(notification => notification.isread);
      } else if (this.filterType === 'unread') {
        filtered = filtered.filter(notification => !notification.isread);
      } else {
        filtered = filtered.filter(notification => notification.type === this.filterType);
      }
    }

    // Update total pages
    this.totalPages = Math.ceil(filtered.length / this.itemsPerPage);

    // Reset to first page if current page is out of bounds
    if (this.currentPage > this.totalPages) {
      this.currentPage = 1;
    }

    return filtered;
  }

  getPaginatedNotifications() {
    const filtered = this.getFilteredNotifications();
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return filtered.slice(startIndex, startIndex + this.itemsPerPage);
  }

  changePage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxVisiblePages = 5;

    if (this.totalPages <= maxVisiblePages) {
      // Show all pages if total pages is less than max visible
      for (let i = 1; i <= this.totalPages; i++) {
        pages.push(i);
      }
    } else {
      // Show pages around current page
      let start = Math.max(1, this.currentPage - Math.floor(maxVisiblePages / 2));
      let end = start + maxVisiblePages - 1;

      if (end > this.totalPages) {
        end = this.totalPages;
        start = Math.max(1, end - maxVisiblePages + 1);
      }

      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
    }

    return pages;
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
        console.log("getNotification loaded:", data.data.notification);
      },
      error: (error) => {
        console.error('Error loading headers:', error);
      }
    });
  }
}
