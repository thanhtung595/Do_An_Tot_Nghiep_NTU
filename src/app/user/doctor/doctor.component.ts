import { Component } from '@angular/core';

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css'],
})
export class DoctorComponent {
  // Dữ liệu giả lập danh sách bác sĩ
  doctors = [
    {
      id: 1,
      image:
        'https://static.vecteezy.com/system/resources/thumbnails/026/375/249/small_2x/ai-generative-portrait-of-confident-male-doctor-in-white-coat-and-stethoscope-standing-with-arms-crossed-and-looking-at-camera-photo.jpg',
      name: 'Dr. John Doe',
      phone: '123-456-7890',
      specialty: 'Bác sĩ đa khoa',
      experience: '10 năm kinh nghiệm',
      schedule: 'Thứ 2 - Thứ 6: 8:00 AM - 5:00 PM',
    },
    {
      id: 2,
      image:
        'https://static.vecteezy.com/system/resources/thumbnails/026/375/249/small_2x/ai-generative-portrait-of-confident-male-doctor-in-white-coat-and-stethoscope-standing-with-arms-crossed-and-looking-at-camera-photo.jpg',
      name: 'Dr. Jane Smith',
      phone: '987-654-3210',
      specialty: 'Bác sĩ tim mạch',
      experience: '8 năm kinh nghiệm',
      schedule: 'Thứ 3 - Thứ 5: 9:00 AM - 6:00 PM',
    },
    {
      id: 3,
      image:
        'https://static.vecteezy.com/system/resources/thumbnails/026/375/249/small_2x/ai-generative-portrait-of-confident-male-doctor-in-white-coat-and-stethoscope-standing-with-arms-crossed-and-looking-at-camera-photo.jpg',
      name: 'Dr. Emily Johnson',
      phone: '555-555-5555',
      specialty: 'Bác sĩ nhi khoa',
      experience: '12 năm kinh nghiệm',
      schedule: 'Thứ 4 - Thứ 7: 10:00 AM - 7:00 PM',
    },
  ];

  selectedDoctor: any = null; // Bác sĩ được chọn để xem chi tiết
  isBooking: boolean = false; // Trạng thái đặt lịch khám
  showModal: boolean = false; // Hiển thị popup

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
    if (bookingForm.valid) {
      alert(
        `Đã đặt lịch khám với ${this.selectedDoctor.name} vào ${bookingForm.value.date} lúc ${bookingForm.value.time}.`
      );
      this.closeModal(); // Đóng popup sau khi đặt lịch
    }
  }
}
