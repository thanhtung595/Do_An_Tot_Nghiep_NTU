import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

interface MedicalRecord {
  id: number;
  patientName: string;
  age: number;
  gender: string;
  address: string;
  phone: string;
  symptoms: string;
  diagnosis: string;
  treatment: string;
  medications: string[];
  notes: string;
  nextAppointment?: string;
}

@Component({
  selector: 'app-doctor-edit-medical-record',
  templateUrl: './edit-medical-record.component.html',
  styleUrls: ['./edit-medical-record.component.css']
})
export class DoctorEditMedicalRecordComponent implements OnInit {
  record: MedicalRecord = {
    id: 1,
    patientName: 'Nguyễn Văn A',
    age: 35,
    gender: 'Nam',
    address: '123 Đường ABC, Quận XYZ, TP.HCM',
    phone: '0123456789',
    symptoms: 'Sốt cao, đau họng, ho khan',
    diagnosis: 'Viêm họng cấp',
    treatment: 'Điều trị nội khoa',
    medications: ['Paracetamol 500mg', 'Amoxicillin 500mg'],
    notes: 'Bệnh nhân cần nghỉ ngơi và uống nhiều nước',
    nextAppointment: '2024-03-22'
  };

  newMedication: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Trong thực tế, lấy ID từ route params và gọi API để lấy dữ liệu
    const id = this.route.snapshot.paramMap.get('id');
    console.log('Editing record:', id);
  }

  addMedication(): void {
    if (this.newMedication.trim()) {
      this.record.medications.push(this.newMedication.trim());
      this.newMedication = '';
    }
  }

  removeMedication(index: number): void {
    this.record.medications.splice(index, 1);
  }

  saveRecord(): void {
    // Trong thực tế, gọi API để lưu dữ liệu
    console.log('Saving record:', this.record);
    this.router.navigate(['/doctor/patient-history']);
  }

  cancel(): void {
    this.router.navigate(['/doctor/patient-history']);
  }
} 