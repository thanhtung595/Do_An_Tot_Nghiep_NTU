import { Component } from '@angular/core';
import { DoctorApiService } from '@app/services/api_admin/doctor/doctor.api.service'
import { API_BASE_URL } from '@app/constants'

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css'],
})
export class DoctorComponent {
  constructor(private doctorApiService: DoctorApiService) { }
  ngOnInit(): void {
    this.getaAllDoctor();
  }
  getImageUrl(imgPath: string): string {
    return `${API_BASE_URL}${imgPath}`;
  }

  doctors : any[] = [];
  getaAllDoctor(){
    this.doctorApiService.getAllDoctor().subscribe({
      next: (data) => {
        this.doctors = data.doctors;
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
  specialties: string[] = ['Bác sĩ đa khoa', 'Bác sĩ tim mạch', 'Bác sĩ nhi khoa'];

  // Lọc danh sách bác sĩ
  get filteredDoctors() {
    return this.doctors.filter(doctor => {
      const matchesSearch = doctor.name.toLowerCase().includes(this.searchText.toLowerCase());
      const matchesSpecialty = this.selectedSpecialty ? doctor.specialty === this.selectedSpecialty : true;
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
  }

  // Đặt lịch khám (giả lập)
  bookAppointment(bookingForm: any) {
    const data =
    {
      doctorID: this.selectedDoctor.id,
      diagnosis: bookingForm.value.diagnosis,
      workday: bookingForm.value.workday,
      timeOnline: bookingForm.value.timeOnline,
      note: bookingForm.value.note,
    };
    if (bookingForm.valid) {
      console.log("data: ",data);
      this.doctorApiService.createAppointments(data).subscribe({
        next: (data) => {
          console.log(data)
          console.log(data.status)
          alert("Bạn đã đặt lịch khám thành công.");
          this.closeModal(); // Đóng popup sau khi đặt lịch
        },
        error: (error) => {
          console.error('Error fetching bookAppointment:', error);
          if(error.status == 401){
            alert("Bạn cần đăng nhập trước khi đặt lịch");
            return;
          }
          if(error.status == 400){
            alert(error.error.msg);
            return;
          }
        }
      });
    }
    // if (bookingForm.valid) {

    //   // alert(
    //   //   `Đã đặt lịch khám với ${this.selectedDoctor.name} vào ${bookingForm.value.date} lúc ${bookingForm.value.time}.`
    //   // );
    //   // this.closeModal(); // Đóng popup sau khi đặt lịch
    // }
  }
}
