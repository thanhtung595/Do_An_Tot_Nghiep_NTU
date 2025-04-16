import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  msgError = '';

  constructor(private router: Router, private authApiService : AuthApiService) {}

  onSubmit() {
    // Xử lý đăng nhập ở đây
    console.log('username:', this.username);
    console.log('password:', this.password);


    this.authApiService.login(this.username, this.password).subscribe({
      next: (data) => {
        this.router.navigate(['/home']).then(() => {
          window.location.href = '/home';
        });
      },
      error: (error) => {
        this.msgError = error.error.msg;
        console.error('Error fetching users:', this.msgError);
      }
    });
  }
}
