import { Component } from '@angular/core';
import { ServiceApiService } from '@app/services/api_admin/services/service.api.service'
import { ToastService } from '@app/services/toast/toast.service';
import { ConfirmDialogService } from '@app/services/dialog/confirm-dialog.service';
import { Router } from '@angular/router';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { clearAccessToken } from '@app/services/token/TokenService';

@Component({
  selector: 'app-service',
  templateUrl: './service.component.html',
  styleUrls: ['./service.component.css']
})
export class ServiceComponent {

  constructor(private serviceApiService: ServiceApiService,
    private confirmDialogService: ConfirmDialogService,
    private toastService: ToastService,
    private router: Router,
    private authService: AuthApiService,
  ) { }

  services : any[] = [];

  ngOnInit(): void {
    this.requiredRole();
    this.getAllService();
  };


  searchName = '';
  totalservice = 0;
  msgError = '';

  isModalOpen = false;
  modalType: 'add' | 'edit' | 'delete' | null = null;
  selectedService: any = {};

  // Phân trang
  currentPage = 1;
  pageSize = 10;

  get totalPages() {
    return Math.ceil(this.filteredservices().length / this.pageSize);
  }

  filteredservices() {
    const listFilter = this.services.filter(service => {
      const nameMatch = service.name.toLowerCase().includes(this.searchName.toLowerCase());
      return nameMatch;
    });
    this.totalservice = listFilter.length;
    return listFilter;
  }

  paginatedservices() {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.filteredservices().slice(start, start + this.pageSize);
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

  openModal(type: 'add' | 'edit' | 'delete', service: any = null) {
    this.modalType = type;
    this.isModalOpen = true;
    this.selectedService = service ? { ...service } : {};
  }

  closeModal() {
    this.msgError = '';
    this.isModalOpen = false;
    this.selectedService = {};
    this.toastService.info('Đã hủy thao tác.');
  }

  addService() {
    const payload = this.selectedService;
    console.log("payload", payload);
    this.serviceApiService.createService(payload).subscribe({
      next: (data) => {
        this.getAllService();
        this.closeModal();
        this.toastService.success('Đã thêm thành công');
      },
      error: (error) => {
        console.error('Error fetching:', error);
        this.toastService.error(error.error.message);
      },
    });
  }

  updateService() {
    const payload = this.selectedService;
    console.log("payload", payload);
    this.serviceApiService.updateService(payload).subscribe({
      next: (data) => {
        this.getAllService();
        this.closeModal();
        this.toastService.success('Đã cập nhật thành công');
      },
      error: (error) => {
        this.getAllService();
        this.toastService.error(error.error.message);
      },
    });
  }

  deleteService() {
    this.serviceApiService.deleteService(this.selectedService.id).subscribe({
      next: (data) => {
        this.getAllService();
        this.closeModal();
        this.toastService.success('Đã xóa thành công');
      },
      error: (error) => {
        this.getAllService();
        this.toastService.error(error.error.message);
      }
    });
  }

  getAllService(){
    this.serviceApiService.getAllServices().subscribe({
      next: (data) => {
        this.services = data.data.service;
        console.log("services", this.services);
      },
      error: (error) => {
        console.error('Error fetching services:', error);
      }
    });
  }

  toLinkServices(){
    this.router.navigate(['/admin/services']);
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
