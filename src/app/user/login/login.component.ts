import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { saveAccessToken } from '@app/services/token/TokenService';

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
        saveAccessToken(data.data.token.accessToken);

        this.router.navigate(['/home']).then(() => {
          window.location.href = '/home';
        });
      },
      error: (error) => {
        this.msgError = error.error.message;
        console.log(this.msgError)
        console.error('Error fetching users:', this.msgError);
      }
    });
  }
}
