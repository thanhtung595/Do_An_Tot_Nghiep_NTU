import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ProfleApiServiceService } from '@app/services/api/profile/profle.api.service.service';
import { API_BASE_URL } from '@app/constants';
import { Router } from '@angular/router';
import { ToastService } from '@app/services/toast/toast.service';
import { clearAccessToken } from '@app/services/token/TokenService';

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
    private router: Router,
    private toastService: ToastService
  ) {}

  private id_user = 0
  private localFile = '';
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
        this.id_user = data.data.user.id;
        this.localFile = data.data.user.image;
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
    // Kiểm tra thông tin trước khi gửi đi
    if (!this.user.fullname || !this.user.gender) {
      this.toastService.warning('Vui lòng điền đầy đủ tên!');
      return;
    }
    console.log('Dữ liệu cập nhật:', this.user);
    this.profleApiServiceService.updateUser(this.user).subscribe({
      next: (data) => {
        this.isEditing = false;
        this.toastService.success('Cập nhật thông tin thành công.');
      },
      error: (error) => {
        this.toastService.error(error.message);
      },
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];

      this.profleApiServiceService.uploadImage(this.selectedFile).subscribe({
        next: (data) => {
          console.log(data.data.image);
          this.user.image = data.data.image;
          this.toastService.success('Cập nhật thông tin thành công.');
        },
        error: (error) => {
          this.user.image = this.localFile;
          this.toastService.error(error.message);
        },
      });
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
    const data = {
      id : this.id_user,
      currentPassword : this.passwordData.currentPassword,
      newPassword : this.passwordData.newPassword
    }

    if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
      this.toastService.warning('Cần nhập lại mật khẩu mới giống nhau');
      return;
    }

    this.profleApiServiceService.editPassword(data).subscribe({
      next: (data) => {
        this.isEditing = false;
        this.toastService.success('Cập nhật thông tin thành công.');
        clearAccessToken();
        window.location.href = '/login';
      },
      error: (error) => {
        this.toastService.error(error.error.message);
      },
    });
  }
}
