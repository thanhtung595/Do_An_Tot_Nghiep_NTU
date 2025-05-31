import { Component, OnInit } from '@angular/core';
import { ProfleApiServiceService } from '@app/services/api/profile/profle.api.service.service';
import { API_BASE_URL } from '@app/constants';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  constructor(
    private profleApiServiceService: ProfleApiServiceService,
    private router: Router
  ) {}

  // Thông tin người dùng
  user = {
    fullname: 'Nguyễn Thanh Tùng',
    email: 'nguyenthanhtung.06112003@gmail.com',
    phonenumber: '083681855',
    address: 'Thanh Hóa - Việt Nam',
    avatar:
      'https://ntt-datn-clinic-management.s3.ap-southeast-1.amazonaws.com/avatar/default.jpg',
    rolename: 'admin', // Có thể là 'patient' hoặc 'doctor'
    image: '',
    dateofbirth: '06-11-2003',
    gender: 'Nam',
  };

  getImageUrl(imgPath: string): string {
    if (this.user.rolename == null || this.user.rolename == 'admin') {
      return `${this.user.avatar}`;
    }
    return `${imgPath}`;
  }

  ngOnInit(): void {
    this.profleApiServiceService.getUser().subscribe({
      next: (data) => {
        this.user = data.data.user;
        console.log('user', this.user);
        this.getAppointment();
      },
      error: (error) => {
        console.error('Error fetching user1:', error);
      },
    });
  }

  // Trạng thái chỉnh sửa
  isEditing: boolean = false;
  selectedFile: File | null = null;

  // Tab hiện tại
  activeTab: string = 'profile';

  // Dữ liệu giả lập
  medicalRecords: any[] = [];
  patientRecords: any[] = [];

  selectedRecord: any = null;
  showRecordDetail: boolean = false; // Hiển thị popup chi tiết hồ sơ

  // Chuyển đổi tab
  setActiveTab(tab: string) {
    this.activeTab = tab;
  }

  // Xem chi tiết hồ sơ bệnh án
  viewMedicalRecord(record: any) {
    this.selectedRecord = record;
    this.showRecordDetail = true; // Hiển thị popup
  }

  // Đóng popup chi tiết hồ sơ
  closeRecordDetail() {
    this.showRecordDetail = false;
    this.selectedRecord = null;
  }

  // Các hàm khác (giữ nguyên từ phần trước)
  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  cancelEdit() {
    this.isEditing = false;
  }

  saveChanges() {
    alert('Thông tin đã được cập nhật!');
    this.isEditing = false;
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.user.avatar = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  uploadAvatar() {
    if (this.selectedFile) {
      alert(`Đã cập nhật ảnh đại diện: ${this.selectedFile.name}`);
    } else {
      alert('Vui lòng chọn ảnh trước khi cập nhật!');
    }
  }

  getAppointment() {
    this.profleApiServiceService.getAppointment().subscribe({
      next: (data) => {
        if (this.user.rolename == 'patient') {
          this.medicalRecords = data.data.appointments;
          console.log('getAppointment', this.medicalRecords);
        } else if (
          this.user.rolename != null &&
          this.user.rolename == 'doctor'
        ) {
          this.patientRecords = data.data.appointments;
          console.log('getAppointment', this.patientRecords);
        }
      },
      error: (error) => {
        console.error('Error fetching user1:', error);
      },
    });
  }

  navigateToPayment(recordId: any): void {
    if (recordId) {
      console.log('Navigating to payment with ID:', recordId); // Debug log
      this.router.navigate(['/payment', recordId]).then(
        (success) => {
          if (!success) {
            console.error('Navigation failed');
            alert('Không thể chuyển đến trang thanh toán. Vui lòng thử lại sau.');
          }
        }
      );
    } else {
      console.error('Record ID is undefined');
      alert('Không thể thực hiện thanh toán. Vui lòng thử lại sau.');
    }
  }
}
