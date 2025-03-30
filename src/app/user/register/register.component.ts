import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  // username: string = '';
  // fullname: string = '';
  // phone: string = '';
  // email: string = '';
  // password: string = '';
  // password_2: string = '';
  msgError = '';

  user = {
    username: '',
    fullname: '',
    phone: '',
    email: '',
    password: '',
    password_2: ''
  };

  constructor(private router: Router, private authApiService: AuthApiService) {}

  onSubmit() {
    // Xử lý đăng nhập ở đây
    const { username, fullname, phone, email, password, password_2 } = this.user;

    if (this.user.password != this.user.password_2) {
      this.msgError = '2 mật khẩu phải giống nhau';
    } else if(this.user.username.length < 6 || this.user.password.length < 6){
      this.msgError = 'Tài khoản mật khẩu phải lớn hơn 6 ký tự';
    }
    else {
      console.log(this.user)
      this.authApiService.register(this.user).subscribe({
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
