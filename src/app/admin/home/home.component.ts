import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { clearAccessToken } from '@app/services/token/TokenService';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class AdminHomeComponent {

  constructor(
      private authService: AuthApiService,
    ) {}

  ngOnInit(): void {
    this.requiredRole();
  }

  // Dữ liệu giả lập
  totalAccounts = 1200;
  totalDoctors = 150;
  totalPatients = 3500;
  totalMedicines = 500;
  totalRevenue = 25000000; // Đơn vị: VND
  completedRecords = 1200;
  inProgressRecords = 150;
  canceledRecords = 50;
  availableClinics = 10;
  activeClinics = 15;
  underMaintenanceClinics = 2;

  requiredRole(){
    this.authService.requiredRoleAdmin().subscribe({
      next: (response) => {
        return;
      },
      error: (error) => {
        clearAccessToken();
        window.location.href = '/login';
      }
    });
  }
}


