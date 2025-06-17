import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DoctorApiService } from '@app/services/api/doctor/doctor.api.service';
import { ToastService } from '@app/services/toast/toast.service';
import { Medicine } from '@app/interfaces/medicine.interface';
import { Service } from '@app/interfaces/service.interface';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { clearAccessToken } from '@app/services/token/TokenService';

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
    services: [],
    status: ''
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

  // Danh sách trạng thái
  statusAppointments = ['Đã duyệt', 'Đã khám', 'Đang điều trị', 'Đợi kết quả', 'Đã khỏi', 'Cần tái khám'];

  showAddMedicineForm = false;
  newMedicine: any = {
    name: '',
    unit: '',
    description: '',
    price: 0
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private doctorApiService: DoctorApiService,
    private toastService: ToastService,
    private authService: AuthApiService,
  ) {}

  ngOnInit(): void {
    this.requiredRole();
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

    this.doctorApiService.getAppointmentRecordById(this.recordId).subscribe({
      next: (data) => {
        console.log(data.data)
        this.medicalRecord = data.data.appointment ?? [];
        this.selectedMedicines = this.medicalRecord.medicines || [];
        this.selectedServices = this.medicalRecord.services || [];
      },
      error: (error) => {
        console.error('Error fetching medicines:', error);
      }
    });
  }

  loadMedicines() {
    this.doctorApiService.getMedicines().subscribe({
      next: (data) => {
        this.medicines = data?.data.medicines ?? [];
        console.log("medicines",this.medicines);
      },
      error: (error) => {
        console.error('Error fetching medicines:', error);
      }
    });

  }

  loadServices() {
    this.doctorApiService.getService().subscribe({
      next: (data) => {
        this.services = data?.data.service ?? [];
        console.log("services",this.services);
      },
      error: (error) => {
        console.error('Error fetching services:', error);
      }
    });
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

    console.log('updatedRecord: ', updatedRecord)

    this.doctorApiService.saveMedicalRecord(updatedRecord).subscribe({
      next: (data) => {
        this.toastService.success('Cập nhật hồ sơ thành công.');
        this.router.navigate(['/doctor/patient-history']);
      },
      error: (error) => {
        this.toastService.error(error.message);
        console.error('Error fetching saveMedicalRecord:', error);
      }
    });

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

  openAddMedicineForm() {
    this.showAddMedicineForm = true;
  }

  closeAddMedicineForm() {
    this.showAddMedicineForm = false;
    this.resetNewMedicineForm();
  }

  resetNewMedicineForm() {
    this.newMedicine = {
      name: '',
      unit: '',
      description: '',
      price: 0
    };
  }

  submitNewMedicine() {
    if (this.newMedicine.name && this.newMedicine.unit) {
      // Thêm thuốc mới vào danh sách
      const medicine: Medicine = {
        id: this.medicines.length + 1, // Tạm thời tạo ID mới
        name: this.newMedicine.name,
        unit: this.newMedicine.unit,
      };

      this.medicines.push(medicine);
      this.selectedMedicines.push(medicine);

      // Đóng form và reset
      this.closeAddMedicineForm();
      this.toastService.success('Thêm thuốc mới thành công');
    }
  }

  requiredRole(){
    this.authService.requiredRoleDoctor().subscribe({
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
