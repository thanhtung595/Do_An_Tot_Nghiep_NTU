import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  email: string = '';
  isSubmitted: boolean = false;
  isLoading: boolean = false;

  constructor(private router: Router) {}

  onSubmit() {
    this.isLoading = true;
    // Mock API call
    setTimeout(() => {
      this.isLoading = false;
      this.isSubmitted = true;
    }, 1500);
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
} 