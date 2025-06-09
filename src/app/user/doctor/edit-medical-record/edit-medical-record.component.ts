import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DoctorApiService } from '@app/services/api/doctor/doctor.api.service';
import { ToastService } from '@app/services/toast/toast.service';
import { Medicine } from '@app/interfaces/medicine.interface';
import { Service } from '@app/interfaces/service.interface';

@Component({
  selector: 'app-edit-medical-record',
  templateUrl: './edit-medical-record.component.html',
  styleUrls: ['./edit-medical-record.component.css']
})
export class DoctorEditMedicalRecordComponent implements OnInit {
  recordId!: number;
  today: string = new Date().toISOString().split('T')[0]; // Format: YYYY-MM-DD
  medicalRecord: any = {
    medicines: [],
    services: []
  };

  // Danh sách thuốc và dịch vụ
  medicines: Medicine[] = [];
  services: Service[] = [];

  // Danh sách đã chọn
  selectedMedicines: Medicine[] = [];
  selectedServices: Service[] = [];

  // Popup state
  showMedicinePopup = false;
  showServicePopup = false;
  searchMedicineText = '';
  searchServiceText = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private doctorApiService: DoctorApiService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.recordId = +id;
      this.loadMedicalRecord();
      this.loadMedicines();
      this.loadServices();
    } else {
      this.toastService.error('Không tìm thấy ID hồ sơ');
      this.router.navigate(['/doctor/patient-history']);
    }
  }

  loadMedicalRecord() {
    // Dữ liệu giả lập cho hồ sơ bệnh án
    this.medicalRecord = {
      id: this.recordId,
      patientname: 'Nguyễn Văn A',
      date: '2024-03-20',
      time: '09:00',
      symptom: 'Sốt cao, đau họng, ho khan',
      diagnosis: 'Viêm họng cấp',
      nextappointment: '2024-03-27', // Thêm ngày tái khám
      medicines: [
        {
          id: 1,
          name: 'Paracetamol 500mg',
          description: 'Thuốc hạ sốt, giảm đau',
          price: 5000,
          unit: 'Viên'
        },
        {
          id: 2,
          name: 'Amoxicillin 500mg',
          description: 'Kháng sinh',
          price: 15000,
          unit: 'Viên'
        }
      ],
      services: [
        {
          id: 1,
          name: 'Khám tổng quát',
          description: 'Khám sức khỏe tổng quát',
          price: 200000
        }
      ]
    };

    this.selectedMedicines = this.medicalRecord.medicines || [];
    this.selectedServices = this.medicalRecord.services || [];
  }

  loadMedicines() {
    // Dữ liệu giả lập cho danh sách thuốc
    this.medicines = [
      {
        id: 1,
        name: 'Paracetamol 500mg',
        description: 'Thuốc hạ sốt, giảm đau',
        price: 5000,
        unit: 'Viên'
      },
      {
        id: 2,
        name: 'Amoxicillin 500mg',
        description: 'Kháng sinh',
        price: 15000,
        unit: 'Viên'
      },
      {
        id: 3,
        name: 'Ibuprofen 400mg',
        description: 'Thuốc giảm đau, kháng viêm',
        price: 8000,
        unit: 'Viên'
      },
      {
        id: 4,
        name: 'Omeprazole 20mg',
        description: 'Thuốc điều trị dạ dày',
        price: 12000,
        unit: 'Viên'
      },
      {
        id: 5,
        name: 'Cetirizine 10mg',
        description: 'Thuốc kháng histamin',
        price: 7000,
        unit: 'Viên'
      }
    ];
  }

  loadServices() {
    // Dữ liệu giả lập cho danh sách dịch vụ
    this.services = [
      {
        id: 1,
        name: 'Khám tổng quát',
        description: 'Khám sức khỏe tổng quát',
        price: 200000
      },
      {
        id: 2,
        name: 'Xét nghiệm máu',
        description: 'Xét nghiệm công thức máu',
        price: 150000
      },
      {
        id: 3,
        name: 'Chụp X-quang',
        description: 'Chụp X-quang phổi',
        price: 300000
      },
      {
        id: 4,
        name: 'Siêu âm',
        description: 'Siêu âm ổ bụng',
        price: 250000
      },
      {
        id: 5,
        name: 'Điện tâm đồ',
        description: 'Đo điện tâm đồ',
        price: 180000
      }
    ];
  }

  openMedicinePopup() {
    this.showMedicinePopup = true;
    this.searchMedicineText = '';
  }

  openServicePopup() {
    this.showServicePopup = true;
    this.searchServiceText = '';
  }

  closeMedicinePopup() {
    this.showMedicinePopup = false;
  }

  closeServicePopup() {
    this.showServicePopup = false;
  }

  getFilteredMedicines(): Medicine[] {
    return this.medicines.filter(medicine =>
      medicine.name.toLowerCase().includes(this.searchMedicineText.toLowerCase())
    );
  }

  getFilteredServices(): Service[] {
    return this.services.filter(service =>
      service.name.toLowerCase().includes(this.searchServiceText.toLowerCase())
    );
  }

  toggleMedicineSelection(medicine: Medicine) {
    const index = this.selectedMedicines.findIndex(m => m.id === medicine.id);
    if (index === -1) {
      this.selectedMedicines.push(medicine);
    } else {
      this.selectedMedicines.splice(index, 1);
    }
  }

  toggleServiceSelection(service: Service) {
    const index = this.selectedServices.findIndex(s => s.id === service.id);
    if (index === -1) {
      this.selectedServices.push(service);
    } else {
      this.selectedServices.splice(index, 1);
    }
  }

  isMedicineSelected(medicine: Medicine): boolean {
    return this.selectedMedicines.some(m => m.id === medicine.id);
  }

  isServiceSelected(service: Service): boolean {
    return this.selectedServices.some(s => s.id === service.id);
  }

  removeMedicine(medicine: Medicine) {
    const index = this.selectedMedicines.findIndex(m => m.id === medicine.id);
    if (index !== -1) {
      this.selectedMedicines.splice(index, 1);
    }
  }

  removeService(service: Service) {
    const index = this.selectedServices.findIndex(s => s.id === service.id);
    if (index !== -1) {
      this.selectedServices.splice(index, 1);
    }
  }

  saveMedicalRecord() {
    const updatedRecord = {
      ...this.medicalRecord,
      medicines: this.selectedMedicines,
      services: this.selectedServices
    };

    // this.doctorApiService.updateMedicalRecord(this.recordId, updatedRecord).subscribe({
    //   next: (response) => {
    //     this.toastService.success('Cập nhật hồ sơ thành công');
    //     this.router.navigate(['/doctor/patient-history']);
    //   },
    //   error: (error) => {
    //     this.toastService.error('Không thể cập nhật hồ sơ');
    //     console.error('Error updating medical record:', error);
    //   }
    // });
  }

  backPatientHistory() {
    this.router.navigate(['/doctor/patient-history']);
  }
}
