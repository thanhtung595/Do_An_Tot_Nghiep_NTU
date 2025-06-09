import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { saveAccessToken } from '@app/services/token/TokenService';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isLoading = false;
  showPassword = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthApiService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false]
    });
  }

  ngOnInit(): void {
    // Kiểm tra xem có thông tin đăng nhập đã lưu không
    const savedUsername = localStorage.getItem('rememberedUsername');
    if (savedUsername) {
      this.loginForm.patchValue({
        username: savedUsername,
        rememberMe: true
      });
    }
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      const { username, password, rememberMe } = this.loginForm.value;

      // Lưu username nếu người dùng chọn "Remember me"
      if (rememberMe) {
        localStorage.setItem('rememberedUsername', username);
      } else {
        localStorage.removeItem('rememberedUsername');
      }

      this.authService.login(username, password).subscribe({
        next: (response) => {
          this.isLoading = false;
          saveAccessToken(response.data.token.accessToken);

          this.router.navigate(['/home']).then(() => {
            window.location.href = '/home';
          });
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = error.error.message || 'Đăng nhập thất bại. Vui lòng thử lại.';
        }
      });
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  loginWithGoogle(): void {
    // Implement Google login
    console.log('Google login clicked');
  }

  loginWithFacebook(): void {
    // Implement Facebook login
    console.log('Facebook login clicked');
  }
}
