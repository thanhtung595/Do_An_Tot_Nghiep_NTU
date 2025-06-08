import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastService } from '@app/services/toast/toast.service';
import { NotificationService } from '@app/services/notification/notification.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private router: Router,
    private toastService: ToastService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.notificationService.connect();
    this.wsQueueNotifications();
  }

  wsQueueNotifications(){
    this.notificationService.connectionStatus$.subscribe(connected => {
      if (connected) {
        // Đợi đến khi kết nối mới subscribe topic
        this.notificationService.subscribeToTopic('/user/queue/notifications').subscribe(data => {
          if (data) {
            this.toastService.info(data.message);
          }
        });
      }
    });
  }

  ngOnDestroy() {
    // Ngắt kết nối WebSocket
    this.notificationService.unsubscribeFromTopic('/user/queue/notifications');
    this.notificationService.disconnect();
  }

  isLoginPage(): boolean {
    return this.router.url === '/login' || this.router.url === '/register' || this.router.url === '/forgot-password';
  }

  isAdmin(): boolean {
    return this.router.url.includes('admin');
  }
}
