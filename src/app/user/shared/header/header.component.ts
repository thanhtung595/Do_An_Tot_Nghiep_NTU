import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { HeaderApiServiceService } from '@app/services/api/header/header.api.service.service';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { clearAccessToken } from '@app/services/token/TokenService';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  headers: any[] = [];
  currentPath: string = '';

  constructor(
    private headerApiService: HeaderApiServiceService,
    private authApiService: AuthApiService,
    private router: Router
  ) {
    // Theo dõi thay đổi route
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.currentPath = event.url;
      console.log('Current path:', this.currentPath);
    });
  }

  ngOnInit(): void {
    this.loadHeaders();
  }

  loadHeaders(): void {
    this.headerApiService.getHeader().subscribe({
      next: (data) => {
        this.headers = data?.data.header ?? [];
        console.log("Headers loaded:", this.headers);
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
