import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ProfleApiServiceService } from '@app/services/api/profile/profle.api.service.service';
import { API_BASE_URL } from '@app/constants';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef;
  selectedFile: File | null = null;
  previewUrl: string = '';

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

  // Tab hiện tại
  activeTab: string = 'profile';

  // Dữ liệu giả lập
  medicalRecords: any[] = [];
  patientRecords: any[] = [];

  selectedRecord: any = null;
  showRecordDetail: boolean = false; // Hiển thị popup chi tiết hồ sơ

  showPasswordModal: boolean = false;
  passwordData = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

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

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];

      // Tạo preview URL
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.previewUrl = e.target.result as string;
        // Cập nhật avatar preview
        if (this.user) {
          this.user.image = this.previewUrl;
          console.log('File to upload:', this.selectedFile);
        }
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  // Hàm này sẽ được gọi khi bạn có API để upload ảnh
  uploadAvatar(): void {
    if (this.selectedFile) {
      // TODO: Implement API call to upload avatar
      console.log('File to upload:', this.selectedFile);
      // Sau khi upload thành công, cập nhật user.avatar với URL mới từ server
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
      this.router.navigate(['/invoice']).then(
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

  showChangePasswordModal(): void {
    this.showPasswordModal = true;
  }

  closePasswordModal(): void {
    this.showPasswordModal = false;
    this.passwordData = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
  }

  changePassword(): void {
    if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
      alert('Mật khẩu mới không khớp!');
      return;
    }

    // TODO: Implement API call to change password
    console.log('Changing password:', this.passwordData);
    this.closePasswordModal();
  }
}
