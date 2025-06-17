import { Component } from '@angular/core';
import { DoctorApiService } from '@app/services/api_admin/doctor/doctor.api.service'
import { API_BASE_URL } from '@app/constants'
import { ToastService } from '@app/services/toast/toast.service';
import { ConfirmDialogService } from '@app/services/dialog/confirm-dialog.service';
import { NotificationService } from '@app/services/notification/notification.service';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { clearAccessToken } from '@app/services/token/TokenService';

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css'],
})
export class DoctorComponent {
  constructor(private doctorApiService: DoctorApiService,
    private toastService: ToastService,
    private notificationService: NotificationService,
    private authService: AuthApiService,
  ) { }

  // Data ws
  latestNotification: any = null;

  ngOnInit(): void {
    this.requiredRole();
    this.getaAllDoctor();
  }

  getImageUrl(imgPath: string): string {
    return `${API_BASE_URL}${imgPath}`;
  }

  doctors : any[] = [];
  getaAllDoctor(){
    this.doctorApiService.getAllDoctor().subscribe({
      next: (data) => {
        this.doctors = data.data.doctors;
        console.log("doctors", this.doctors);
      },
      error: (error) => {
        console.error('Error fetching departments:', error);
      }
    });
  }

  selectedDoctor: any = null; // Bác sĩ được chọn để xem chi tiết
  isBooking: boolean = false; // Trạng thái đặt lịch khám
  showModal: boolean = false; // Hiển thị popup

  // Biến cho tìm kiếm và lọc
  searchText: string = '';
  selectedSpecialty: string = '';
  specialties: string[] = ['Khoa Nội', 'Khoa Ngoại', 'Khoa Nhi', 'Khoa Phụ sản', 'Khoa Tai - Mũi - Họng', 'Khoa Mắt', 'Khoa Da liễu', 'Khoa Răng - Hàm - Mặt', 'Khoa Xét nghiệm', 'Khoa Chẩn đoán hình ảnh', 'Khoa Đông y - Y học cổ truyền'];

  // Lọc danh sách bác sĩ
  get filteredDoctors() {
    return this.doctors.filter(doctor => {
      const matchesSearch = doctor.fullname.toLowerCase().includes(this.searchText.toLowerCase());
      const matchesSpecialty = this.selectedSpecialty ? doctor.name === this.selectedSpecialty : true;
      return matchesSearch && matchesSpecialty;
    });
  }

  // Lọc theo chuyên khoa
  filterBySpecialty(specialty: string) {
    this.selectedSpecialty = specialty;
  }

  // Xóa lọc
  clearFilters() {
    this.selectedSpecialty = '';
    this.searchText = '';
  }

  // Mở popup xem chi tiết bác sĩ
  viewDetail(doctor: any) {
    this.selectedDoctor = doctor;
    this.isBooking = false; // Đảm bảo không hiển thị form đặt lịch
    this.showModal = true; // Hiển thị popup
  }

  // Mở popup đặt lịch khám
  openBookingForm(doctor: any) {
    this.selectedDoctor = doctor;
    this.isBooking = true; // Hiển thị form đặt lịch
    this.showModal = true; // Hiển thị popup
  }

  // Đóng popup
  closeModal() {
    this.showModal = false;
    this.selectedDoctor = null;
    this.isBooking = false;
    this.toastService.info('Đã hủy thao tác.');
  }

  // Đặt lịch khám (giả lập)
  bookAppointment(bookingForm: any) {
    const fieldLabels: { [key: string]: string } = {
      fullname: 'Họ tên bệnh nhân',
      gender: 'Giới tính',
      address: 'Địa chỉ',
      phonenumber: 'Số điện thoại',
      age: 'Ngày sinh',
      diagnosis: 'Triệu chứng',
      note: 'Lời nhắn',
      workday: 'Ngày khám',
      timeOnline: 'Giờ khám'
    };

    const data =
    {
      doctorID: this.selectedDoctor.id,
      doctorName: this.selectedDoctor.fullname,
      diagnosis: bookingForm.value.diagnosis,
      workday: bookingForm.value.workday,
      timeOnline: bookingForm.value.timeOnline,
      note: bookingForm.value.note,
      fullname: bookingForm.value.fullname,
      gender: bookingForm.value.gender,
      address: bookingForm.value.address,
      phonenumber: bookingForm.value.phonenumber,
      age: bookingForm.value.age,
    };

    for (const [key, value] of Object.entries(data)) {
      if (value === null || value === '') {
        const label = fieldLabels[key] || key;
        this.toastService.warning(`${label} chưa điền thông tin.`);
        return;
      }
    }

    this.doctorApiService.createAppointments(data).subscribe({
      next: (data) => {
        this.toastService.success('Bạn đã đặt lịch khám thành công.');
        this.closeModal();
      },
      error: (error) => {
        console.error('Error fetching bookAppointment:', error);
        if(error.status == 401){
          this.toastService.warning('Bạn cần đăng nhập trước khi đặt lịch.');
          return;
        }else if(error.status == 400){
          this.toastService.warning(error.error.message);
          return;
        } else {
          this.toastService.error(error.error.message);
          return;
        }
      }
    });
  }

  requiredRole(){
    this.authService.requiredRolePatient().subscribe({
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
