import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  statusCode: string = '';
  errorMessage: string = '';
  errorDescription: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.statusCode = params['code'];
      this.setErrorDetails();
    });
  }

  private setErrorDetails() {
    switch(this.statusCode) {
      case '401':
        this.errorMessage = 'Unauthorized Access';
        this.errorDescription = 'Bạn không có quyền truy cập vào trang này. Vui lòng đăng nhập để tiếp tục.';
        break;
      case '403':
        this.errorMessage = 'Forbidden';
        this.errorDescription = 'Bạn không có quyền truy cập vào tài nguyên này.';
        break;
      case '404':
        this.errorMessage = 'Page Not Found';
        this.errorDescription = 'Trang bạn đang tìm kiếm không tồn tại hoặc đã bị di chuyển.';
        break;
      case '500':
        this.errorMessage = 'Internal Server Error';
        this.errorDescription = 'Đã xảy ra lỗi máy chủ. Vui lòng thử lại sau.';
        break;
      default:
        this.errorMessage = 'Error';
        this.errorDescription = 'Đã xảy ra lỗi không xác định.';
    }
  }

  goBack() {
    window.history.back();
  }

  goHome() {
    this.router.navigate(['/']);
  }
}
