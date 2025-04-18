import { Component } from '@angular/core';
import { MedicalrecordsApiService } from '@app/services/api_admin/medicalrecords/medicalrecords.api.service';

@Component({
  selector: 'app-user',
  templateUrl: './medicalrecords.component.html',
  styleUrls: ['./medicalrecords.component.css']
})
export class AdminMedicalrecordsComponent {
  constructor(private medicalrecordsApiService: MedicalrecordsApiService) {}

  ngOnInit(): void {
    this.loadMedicalRecords();
  }

  medicalrecords: any[] = [];
  searchPatientName = '';
  searchDoctorName = '';
  searchStatus = '';
  totalRecords = 0;

  isModalOpen = false;
  modalType: 'edit' | 'delete' | null = null;
  selectedRecord: any = {};

  // Phân trang
  currentPage = 1;
  pageSize = 10;

  loadMedicalRecords() {
    this.medicalrecordsApiService.getAllAppointment().subscribe({
      next: (data) => {
        this.medicalrecords = data.data;
        console.log('medicalrecords', this.medicalrecords);
      },
      error: (error) => {
        console.error('Error fetching medicalrecords:', error);
      },
    });
  }

  get totalPages() {
    return Math.ceil(this.filteredRecords().length / this.pageSize);
  }

  filteredRecords() {
    const listFilter = this.medicalrecords.filter(record => {
      const patientMatch = record.patientsname.toLowerCase().includes(this.searchPatientName.toLowerCase());
      const doctorMatch = record.doctorname.toLowerCase().includes(this.searchDoctorName.toLowerCase());
      const statusMatch = this.searchStatus ? record.status === this.searchStatus : true;
      return patientMatch && doctorMatch && statusMatch;
    });
    this.totalRecords = listFilter.length;
    return listFilter;
  }

  paginatedRecords() {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.filteredRecords().slice(start, start + this.pageSize);
  }

  setPageSize(size: Event) {
    const newSize = parseInt((size.target as HTMLSelectElement)?.value ?? "10", 10);
    this.pageSize = newSize;
    this.currentPage = 1;
  }

  nextPage() {
    if (this.currentPage < this.totalPages) this.currentPage++;
  }

  prevPage() {
    if (this.currentPage > 1) this.currentPage--;
  }

  openModal(type: 'edit' | 'delete', record: any) {
    this.modalType = type;
    this.isModalOpen = true;
    this.selectedRecord = { ...record };
  }

  closeModal() {
    this.isModalOpen = false;
    this.selectedRecord = {};
  }

  updateRecord() {
    console.log("updateRecord ",this.selectedRecord)
    // const index = this.doctors.findIndex(u => u.id === this.selectedDoctor.id);
    // if (index !== -1) this.doctors[index] = this.selectedDoctor;
    // console.log(this.selectedDoctor)
    this.medicalrecordsApiService.updateAppointment(this.selectedRecord).subscribe({
      next: (data) => {
        this.loadMedicalRecords();
        this.closeModal();
      },
      error: (error) => {
        this.loadMedicalRecords();
        console.error('Error fetching:', error);
        // this.msgError = error.error.msg;
      },
    });
    // this.medicalrecordsApiService.updateAppointment(this.selectedRecord).subscribe({
    //   next: (response) => {
    //     console.log('Record updated successfully:', response);
    //     this.loadMedicalRecords();
    //     this.closeModal();
    //   },
    //   error: (error) => {
    //     console.error('Error updating record:', error);
    //   }
    // });
  }

  deleteRecord() {
    // this.medicalrecordsApiService.deleteAppointment(this.selectedRecord.id).subscribe({
    //   next: (response) => {
    //     console.log('Record deleted successfully:', response);
    //     this.loadMedicalRecords();
    //     this.closeModal();
    //   },
    //   error: (error) => {
    //     console.error('Error deleting record:', error);
    //   }
    // });
  }
}
