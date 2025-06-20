import { Component } from '@angular/core';
import { HomeApiService } from '@app/services/api/home/home.api.service'
import { API_BASE_URL } from '@app/constants'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {

  constructor(private homeApiService: HomeApiService) { }

  services : any[] = [];
  ngOnInit(): void {
    this.homeApiService.getService().subscribe({
      next: (data) => {
        this.services = data?.data.service ?? [];
        console.log("services",this.services);
      },
      error: (error) => {
        console.error('Error fetching services:', error);
      }
    });
  }

  doctors = [
    {
      image:
        'https://ntt-datn-clinic-management.s3.ap-southeast-1.amazonaws.com/avatar/doctor/phong-kham-thien-an-25476126547.jpg',
      name: 'Nguyễn Văn Thành',
      specialty: 'Khoa Nội',
    },
    {
      image: 'https://ntt-datn-clinic-management.s3.ap-southeast-1.amazonaws.com/avatar/doctor/phong-kham-thien-an-25476176547.jpg',
      name: 'Nguyễn Lan Hương',
      specialty: 'Khoa Y học cổ truyền',
    },
    {
      image: 'https://ntt-datn-clinic-management.s3.ap-southeast-1.amazonaws.com/avatar/doctor/phong-kham-thien-an-25476276547.png',
      name: 'Đỗ Thế Hùng',
      specialty: 'Chẩn đoán hình ảnh',
    },
  ];

  testimonials = [
    {
      name: '	Trần Văn Hùng',
      comment: 'Phòng khám rất chuyên nghiệp, bác sĩ tận tâm.',
    },
    { name: 'Lê Thị Bích Ngọc', comment: 'Dịch vụ tốt, thủ tục nhanh chóng.' },
    {
      name: '	Phạm Quốc Tuấn',
      comment: 'Cơ sở vật chất hiện đại, nhân viên thân thiện.',
    },
  ];
}
