import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  username: string = '';
  password: string = '';
  password_2: string = '';
  msgError = '';

  constructor(private router: Router, private authApiService: AuthApiService) {}

  onSubmit() {
    // Xử lý đăng nhập ở đây
    console.log('Username:', this.username);
    console.log('Password:', this.password);
    console.log('Password:_2', this.password_2);

    if (this.password != this.password_2) {
      this.msgError = '2 mật khẩu phải giống nhau';
    } else if(this.username.length < 6 || this.password.length < 6){
      this.msgError = 'Tài khoản mật khẩu phải lớn hơn 6 ký tự';
    }
    else {
      this.authApiService.register(this.username, this.password).subscribe({
        next: (data) => {
          this.router.navigate(['/login']).then(() => {
            window.location.href = '/login';
          });
        },
        error: (error) => {
          this.msgError = error.error.msg;
          console.error('Error fetching users:', this.msgError);
        },
      });
    }
  }
}
