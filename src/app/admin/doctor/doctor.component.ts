import { Component } from '@angular/core';
import { DoctorApiService } from '@app/services/api_admin/doctor/doctor.api.service'
import { ToastService } from '@app/services/toast/toast.service';
import { ConfirmDialogService } from '@app/services/dialog/confirm-dialog.service';
import { Router } from '@angular/router';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { clearAccessToken } from '@app/services/token/TokenService';

@Component({
  selector: 'app-doctor',
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css']
})
export class DoctorAdminComponent {

  constructor(private doctorApiService: DoctorApiService,
    private confirmDialogService: ConfirmDialogService,
    private toastService: ToastService,
    private router: Router,
    private authService: AuthApiService,
  ) { }
  selecteddepartmentid: number | null = null;
  departments : any[] = [];

  doctors : any[] = [];

  ngOnInit(): void {
    this.requiredRole();
    this.doctorApiService.getAllDepartments().subscribe({
      next: (data) => {
        this.departments = data.data.departments;
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
      // department: this.departments.find(dep => dep.id === this.selecteddepartmentid) || null
    };
  }

  closeModal() {
    this.msgError = '';
    this.isModalOpen = false;
    this.selectedDoctor = {};
    this.toastService.info('Đã hủy thao tác.');
  }

  adddoctor() {
    this.selectedDoctor.departmentid = Number(this.selectedDoctor.departmentid);
    this.selectedDoctor.experience = Number(this.selectedDoctor.experience);
    const payload = this.selectedDoctor;
    console.log("payload", payload);
    this.doctorApiService.createDoctor(payload).subscribe({
      next: (data) => {
        this.doctors.push(this.selectedDoctor);
        this.getaAllDoctor();
        this.closeModal();
        this.toastService.success('Đã thêm thành công');
      },
      error: (error) => {
        console.error('Error fetching:', error);
        this.toastService.error(error.error.message);
      },
    });
  }

  updatedoctor() {
    this.selectedDoctor.departmentid = Number(this.selectedDoctor.departmentid);
    this.selectedDoctor.experience = Number(this.selectedDoctor.experience);
    const payload = this.selectedDoctor;
    console.log("payload", payload);
    const index = this.doctors.findIndex(u => u.id === this.selectedDoctor.id);
    if (index !== -1) this.doctors[index] = this.selectedDoctor;
    console.log(this.selectedDoctor)
    this.doctorApiService.updateDoctor(payload).subscribe({
      next: (data) => {
        this.getaAllDoctor();
        this.closeModal();
        this.toastService.success('Đã cập nhật thành công');
      },
      error: (error) => {
        this.getaAllDoctor();
        this.toastService.error(error.error.message);
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
        this.doctors = data.data.doctors;
        console.log("doctors", this.doctors);
      },
      error: (error) => {
        console.error('Error fetching departments:', error);
      }
    });
  }

  toLinkDoctors(){
    this.router.navigate(['/admin/users']);
  }

  requiredRole(){
    this.authService.requiredRoleAdmin().subscribe({
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
