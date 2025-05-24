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
      'https://scontent.fhan2-5.fna.fbcdn.net/v/t1.6435-9/125408833_193036345700650_8843732697979880226_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=trlOeXMYQx0Q7kNvgG9ZKZE&_nc_oc=Adnz7KGwoGaUzCDVoL9aL5hzGaAknviLiYtJIIipCa2S9nVKI0Tk4lkVrNOKCUlc4tY&_nc_zt=23&_nc_ht=scontent.fhan2-5.fna&_nc_gid=FRTsjvCLJXo1CNUqG0F-Gw&oh=00_AYHJ2VBB7Teyhcw6y0fB9qeLNpwjR_yTvKNng3ppeizHUw&oe=68120F1E',
    rolename: 'admin', // Có thể là 'patient' hoặc 'doctor'
    image: '',
    dateofbirth: '06-111-2003',
    gender: 'Nam',
  };

  getImageUrl(imgPath: string): string {
    if (this.user.rolename == null || this.user.rolename == 'admin') {
      return `${this.user.avatar}`;
    }
    return `${API_BASE_URL}${imgPath}`;
  }

  ngOnInit(): void {
    this.profleApiServiceService.getUser().subscribe({
      next: (data) => {
        this.user = data;
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
          this.medicalRecords = data.data;
          console.log('getAppointment', this.medicalRecords);
        } else if (
          this.user.rolename != null &&
          this.user.rolename == 'doctor'
        ) {
          this.patientRecords = data.data;
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
