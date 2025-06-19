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
  availableTimes: string[] = [];
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
  availableWorkdays: { value: string, label: string }[] = [];

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
    this.availableTimes = this.generateAvailableTimes(doctor.timeonline);
    this.availableWorkdays = this.generateAvailableWorkdays(doctor.workdays);
    this.isBooking = false; // Đảm bảo không hiển thị form đặt lịch
    this.showModal = true; // Hiển thị popup
  }

  // Mở popup đặt lịch khám
  openBookingForm(doctor: any) {
    this.selectedDoctor = doctor;
    this.availableTimes = this.generateAvailableTimes(doctor.timeonline);
    this.availableWorkdays = this.generateAvailableWorkdays(doctor.workdays);
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

  // Hàm chuyển đổi chuỗi giờ thành mảng các khung giờ 30 phút
  generateAvailableTimes(timeRange: string): string[] {
    if (!timeRange) return [];
    // timeRange ví dụ: '08:00 AM - 18:00 PM'
    const [start, end] = timeRange.split(' - ');
    if (!start || !end) return [];
    const parseTime = (t: string) => {
      const [time, meridian] = t.split(' ');
      let [hour, minute] = time.split(':').map(Number);
      if (meridian === 'PM' && hour < 12) hour += 12;
      if (meridian === 'AM' && hour === 12) hour = 0;
      return { hour, minute };
    };
    const pad = (n: number) => n.toString().padStart(2, '0');
    const startTime = parseTime(start);
    const endTime = parseTime(end);
    const result: string[] = [];
    let h = startTime.hour, m = startTime.minute;
    while (h < endTime.hour || (h === endTime.hour && m <= endTime.minute)) {
      // Hiển thị dạng 08:00, 08:30, ... (24h)
      result.push(`${pad(h)}:${pad(m)}`);
      m += 60;
      if (m >= 60) { h++; m = 0; }
    }
    return result;
  }

  generateAvailableWorkdays(workdays: string): { value: string, label: string }[] {
    if (!workdays) return [];
    const weekdayMap: { [key: string]: number } = {
      'Chủ nhật': 0,
      'Thứ 2': 1,
      'Thứ 3': 2,
      'Thứ 4': 3,
      'Thứ 5': 4,
      'Thứ 6': 5,
      'Thứ 7': 6
    };
    // Lấy các số thứ trong tuần từ chuỗi workdays
    const allowedWeekdays = workdays.split(' - ').map(w => w.trim()).map(w => weekdayMap[w]).filter(x => x !== undefined);
    const today = new Date();
    const result: { value: string, label: string }[] = [];
    for (let i = 0; i < 31; i++) {
      const d = new Date(today.getFullYear(), today.getMonth(), today.getDate() + i);
      if (allowedWeekdays.includes(d.getDay())) {
        const value = `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;
        const weekdayLabel = Object.keys(weekdayMap).find(k => weekdayMap[k] === d.getDay()) || '';
        const label = `${weekdayLabel} ${d.getDate().toString().padStart(2, '0')}/${(d.getMonth()+1).toString().padStart(2, '0')}/${d.getFullYear()}`;
        result.push({ value, label });
      }
    }
    return result;
  }
}
