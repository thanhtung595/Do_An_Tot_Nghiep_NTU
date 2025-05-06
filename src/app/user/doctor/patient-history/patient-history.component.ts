import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface PatientRecord {
  id: number;
  patientName: string;
  date: string;
  diagnosis: string;
  status: string;
  nextAppointment?: string;
}

@Component({
  selector: 'app-doctor-patient-history',
  templateUrl: './patient-history.component.html',
  styleUrls: ['./patient-history.component.css']
})
export class DoctorPatientHistoryComponent implements OnInit {
  patientRecords: PatientRecord[] = [
    {
      id: 1,
      patientName: 'Nguyễn Văn A',
      date: '2024-03-15',
      diagnosis: 'Cảm cúm',
      status: 'Đã khám',
      nextAppointment: '2024-03-22'
    },
    {
      id: 2,
      patientName: 'Trần Thị B',
      date: '2024-03-14',
      diagnosis: 'Đau dạ dày',
      status: 'Đang điều trị',
      nextAppointment: '2024-03-21'
    },
    {
      id: 3,
      patientName: 'Lê Văn C',
      date: '2024-03-13',
      diagnosis: 'Viêm họng',
      status: 'Đã khỏi'
    }
  ];

  constructor(private router: Router) {}

  ngOnInit(): void {}

  editRecord(id: number): void {
    this.router.navigate(['/doctor/edit-medical-record', id]);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Đã khám':
        return 'status-completed';
      case 'Đang điều trị':
        return 'status-in-progress';
      case 'Đã khỏi':
        return 'status-cured';
      default:
        return '';
    }
  }
} 