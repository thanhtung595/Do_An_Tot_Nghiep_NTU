import { Component } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  // Thông tin người dùng
  user = {
    name: 'Nguyễn Văn A',
    email: 'nguyenvana@gmail.com',
    phone: '0123456789',
    address: '123 Đường ABC, Quận 1, TP.HCM',
    avatar: 'https://via.placeholder.com/150',
    role: 'patient', // Có thể là 'patient' hoặc 'doctor'
  };

  // Trạng thái chỉnh sửa
  isEditing: boolean = false;
  selectedFile: File | null = null;

  // Tab hiện tại
  activeTab: string = 'profile';

  // Dữ liệu giả lập
  medicalRecords = [
    {
      date: '2023-10-01',
      doctor: 'Dr. John Doe',
      diagnosis: 'Cảm cúm',
      notes: 'Nghỉ ngơi và uống thuốc đều đặn.',
    },
    {
      date: '2023-09-15',
      doctor: 'Dr. Jane Smith',
      diagnosis: 'Đau dạ dày',
      notes: 'Hạn chế ăn đồ cay nóng.',
    },
  ];

  patientRecords = [
    {
      name: 'Nguyễn Văn B',
      date: '2023-10-05',
      diagnosis: 'Viêm họng',
    },
    {
      name: 'Trần Thị C',
      date: '2023-09-20',
      diagnosis: 'Đau đầu',
    },
  ];

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
}
