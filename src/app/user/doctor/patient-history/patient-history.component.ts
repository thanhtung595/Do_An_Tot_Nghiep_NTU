import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DoctorApiService } from '@app/services/api/doctor/doctor.api.service'
import { ToastService } from '@app/services/toast/toast.service';
import { ConfirmDialogService } from '@app/services/dialog/confirm-dialog.service';

interface PatientRecord {
  id: number;
  patientname: string;
  date: string;
  time: string;
  symptom: string;
  diagnosis: string;
  status: string;
  nextappointment?: string;
}

@Component({
  selector: 'app-doctor-patient-history',
  templateUrl: './patient-history.component.html',
  styleUrls: ['./patient-history.component.css']
})
export class DoctorPatientHistoryComponent implements OnInit {
  patientRecords: PatientRecord[] = [];
  filteredRecords: PatientRecord[] = [];
  paginatedRecords: PatientRecord[] = [];

  // Phân trang
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 1;

  // Tìm kiếm
  searchName: string = '';
  searchStatus: string = '';

  statusList: string[] = [
    'Đợi duyệt',
    'Đã duyệt',
  ];

  statusListSearch: string[] = [
    'Đợi duyệt',
    'Đã duyệt',
    'Đã khám',
    'Đang điều trị',
    'Đợi kết quả',
    'Đã khỏi',
    'Cần tái khám'
  ];

  constructor(
    private router: Router,
    private doctorApiService: DoctorApiService,
    private toastService: ToastService,
    private confirmDialogService: ConfirmDialogService
  ) {}

  ngOnInit(): void {
    this.getaAllDoctor();
  }

  getaAllDoctor() {
    this.doctorApiService.getPatientHistory().subscribe({
      next: (data) => {
        this.patientRecords = data.data.appointments;
        this.filteredRecords = [...this.patientRecords];
        this.updatePagination();
      },
      error: (error) => {
        console.error('Error fetching departments:', error);
      }
    });
  }

  onSearch() {
    this.filteredRecords = this.patientRecords.filter(record => {
      const nameMatch = record.patientname.toLowerCase().includes(this.searchName.toLowerCase());
      const statusMatch = !this.searchStatus || record.status === this.searchStatus;
      return nameMatch && statusMatch;
    });
    this.currentPage = 1;
    this.updatePagination();
  }

  updatePagination() {
    this.totalPages = Math.ceil(this.filteredRecords.length / this.itemsPerPage);
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedRecords = this.filteredRecords.slice(startIndex, endIndex);
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.updatePagination();
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxPagesToShow = 5;
    let startPage = Math.max(1, this.currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(this.totalPages, startPage + maxPagesToShow - 1);

    if (endPage - startPage + 1 < maxPagesToShow) {
      startPage = Math.max(1, endPage - maxPagesToShow + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }

  editRecord(id: number): void {
    this.router.navigate(['/doctor/edit-medical-record', id]);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Đợi duyệt':
        return 'status-pending';
      case 'Đã duyệt':
        return 'status-approved';
      case 'Đã khám':
        return 'status-examined';
      case 'Đang điều trị':
        return 'status-treating';
      case 'Đợi kết quả':
        return 'status-waiting-result';
      case 'Đã khỏi':
        return 'status-recovered';
      case 'Cần tái khám':
        return 'status-recheck';
      default:
        return 'status-unknown';
    }
  }

  onStatusChange(record: any) {
    this.confirmDialogService.show({
      title: 'Xác nhận cập nhật',
      message: 'Bạn có chắc chắn muốn cập nhật lại trạng thái này.',
      confirmText: 'Cập nhật',
      cancelText: 'Hủy',
      onConfirm: () => {
        const data = {
          id: record.id,
          patientid: record.patientid,
          doctorid: record.doctorid,
          date: record.date,
          time: record.time,
          patientname: record.patientname,
          doctorname: record.doctorname,
          status: record.status
        }

        this.doctorApiService.updateStatusAppointment(data).subscribe({
          next: (data) => {
            let text = 'Đã cập nhật ID :' + record.id + ' thành :' + record.status;
            this.toastService.success(text);
            this.onSearch(); // Refresh the filtered list
          },
          error: (error) => {
            this.toastService.error(error.data.message);
            console.error('Error fetching departments:', error);
          }
        });
      },
      onCancel: () => {
        this.getaAllDoctor();
        this.toastService.info('Đã hủy thao tác cập nhật');
      }
    });
  }
}
