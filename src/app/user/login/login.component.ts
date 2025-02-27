import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private router: Router) {}

  onSubmit() {
    // Xử lý đăng nhập ở đây
    console.log('Username:', this.username);
    console.log('Password:', this.password);

    // Gọi API đăng nhập hoặc xử lý logic đăng nhập
    // Ví dụ: this.authService.login(this.username, this.password);
    if (this.username === 'admin' && this.password === 'admin') {
      alert('Đăng nhập thành công!');
      this.router.navigate(['/home']);
    } else {
      alert('Sai email hoặc mật khẩu!');
    }
  }
}
