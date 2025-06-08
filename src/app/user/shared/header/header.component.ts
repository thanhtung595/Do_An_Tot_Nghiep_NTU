import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { HeaderApiServiceService } from '@app/services/api/header/header.api.service.service';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { clearAccessToken } from '@app/services/token/TokenService';
import { NotificationService } from '@app/services/notification/notification.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  headers: any[] = [];
  currentPath: string = '';
  notificationBadge = 0;

  constructor(
    private headerApiService: HeaderApiServiceService,
    private authApiService: AuthApiService,
    private router: Router,
    private notificationService: NotificationService
  ) {
    // Theo dõi thay đổi route
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.currentPath = event.url;
      // console.log('Current path:', this.currentPath);
    });
  }

  ngOnInit(): void {
    this.loadHeaders();
    this.getCountIsRead();
    // this.notificationService.connect();
    this.wsCountIsreadNotifications();
  }

  wsCountIsreadNotifications(){
      this.notificationService.connectionStatus$.subscribe(connected => {
        if (connected) {
          // Đợi đến khi kết nối mới subscribe topic
          this.notificationService.subscribeToTopic('/user/queue/count/isread').subscribe(data => {
            if (data) {
              this.notificationBadge = data.data.notification;
            }
          });
        }
      });
    }

  ngOnDestroy() {
    // Ngắt kết nối WebSocket
    this.notificationService.unsubscribeFromTopic('/user/queue/count/isread');
    this.notificationService.disconnect();
  }

  loadHeaders(): void {
    this.headerApiService.getHeader().subscribe({
      next: (data) => {
        this.headers = data?.data.header ?? [];
        // console.log("Headers loaded:", this.headers);
      },
      error: (error) => {
        console.error('Error loading headers:', error);
      }
    });
  }

  getCountIsRead(): void {
    this.headerApiService.getCountIsRead().subscribe({
      next: (data) => {
        this.notificationBadge = data.data.notification;
      },
      error: (error) => {
        console.error('Error loading headers:', error);
      }
    });
  }

  isActive(url: string): boolean {
    // Xử lý URL trang chủ
    if (url === '/') {
      return this.currentPath === '/' || this.currentPath === '/home';
    }

    // Xử lý các URL khác
    // Loại bỏ dấu / ở đầu và cuối URL để so sánh chính xác hơn
    const currentPath = this.currentPath.replace(/^\/|\/$/g, '');
    const menuUrl = url.replace(/^\/|\/$/g, '');

    // Nếu URL hiện tại trống hoặc là 'home', kiểm tra xem có phải trang chủ không
    if ((!currentPath || currentPath === 'home') && menuUrl === '') {
      return true;
    }

    // So sánh URL
    return currentPath === menuUrl;
  }

  onClickLogout(data: any, event: Event): void {
    if (data.url === "logout") {
      event.preventDefault();
      clearAccessToken();
      window.location.href = '/home';
    }
  }
}
