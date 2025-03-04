import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class AdminHomeComponent {
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
}
