import { Component } from '@angular/core';
import { DoctorApiService } from '@app/services/api_admin/doctor/doctor.api.service'

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css']
})
export class DoctorAdminComponent {

  constructor(private doctorApiService: DoctorApiService) { }
  selecteddepartmentid: number | null = null;
  departments : any[] = [];

  doctors : any[] = [];

  ngOnInit(): void {
    this.doctorApiService.getAllDepartments().subscribe({
      next: (data) => {
        this.departments = data.departments;
        console.log("departments", this.departments);
        // Gán giá trị mặc định là id của phần tử đầu tiên
        if (this.departments.length > 0) {
          this.selecteddepartmentid = this.departments[0].id;
        }
      },
      error: (error) => {
        console.error('Error fetching departments:', error);
      }
    });

    this.getaAllDoctor();
  };


  searchName = '';
  searchEmail = '';
  searchRole = '';
  totaldoctor = 0;
  msgError = '';

  isModalOpen = false;
  modalType: 'add' | 'edit' | 'delete' | null = null;
  selectedDoctor: any = {};

  // Phân trang
  currentPage = 1;
  pageSize = 10;

  get totalPages() {
    return Math.ceil(this.filtereddoctors().length / this.pageSize);
  }

  filtereddoctors() {
    const listFilter = this.doctors.filter(doctor => {
      const nameMatch = doctor.username.toLowerCase().includes(this.searchName.toLowerCase());
      const emailMatch = doctor.email.toLowerCase().includes(this.searchEmail.toLowerCase());
      const roleMatch = this.searchRole ? doctor.name === this.searchRole : true;
      return nameMatch && emailMatch && roleMatch;
    });
    this.totaldoctor = listFilter.length;
    return listFilter;
  }

  paginateddoctors() {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.filtereddoctors().slice(start, start + this.pageSize);
  }

  setPageSize(size: Event) {
    const newSize = parseInt((size.target as HTMLSelectElement)?.value ?? "10", 10);
    this.pageSize = newSize;
    this.currentPage = 1; // Reset về trang đầu
  }

  nextPage() {
    if (this.currentPage < this.totalPages) this.currentPage++;
  }

  prevPage() {
    if (this.currentPage > 1) this.currentPage--;
  }

  openModal(type: 'add' | 'edit' | 'delete', doctor: any = null) {
    this.modalType = type;
    this.isModalOpen = true;
    this.selectedDoctor = doctor ? { ...doctor } :
    {
      // username: this.selectedDoctor.username,
      // password: this.selectedDoctor.password,
      // fullname: this.selectedDoctor.fullName,
      // email: this.selectedDoctor.email,
      // phone: this.selectedDoctor.phoneNumber,
      // address: this.selectedDoctor.address,
      // experience: this.selectedDoctor.experience,
      // departmentid: this.selecteddepartmentid
    };
  }

  closeModal() {
    this.msgError = '';
    this.isModalOpen = false;
    this.selectedDoctor = {};
  }

  adddoctor() {
    console.log(this.selectedDoctor)
    this.doctorApiService.createDoctor(this.selectedDoctor).subscribe({
      next: (data) => {
        this.getaAllDoctor();
        this.closeModal();
      },
      error: (error) => {
        console.error('Error fetching:', error);
        this.msgError = error.error.msg;
      },
    });
    this.doctors.push(this.selectedDoctor);
    this.closeModal();
  }

  updatedoctor() {
    const index = this.doctors.findIndex(u => u.id === this.selectedDoctor.id);
    if (index !== -1) this.doctors[index] = this.selectedDoctor;
    console.log(this.selectedDoctor)
    this.doctorApiService.updateDoctor(this.selectedDoctor).subscribe({
      next: (data) => {
        this.getaAllDoctor();
        this.closeModal();
      },
      error: (error) => {
        this.getaAllDoctor();
        console.error('Error fetching:', error);
        this.msgError = error.error.msg;
      },
    });
  }

  deletedoctor() {
    this.doctors = this.doctors.filter(u => u.id !== this.selectedDoctor.id);
    this.closeModal();
  }

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
}
