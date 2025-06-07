import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserApiService } from '@app/services/api_admin/user/user.api.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class AdminUserComponent {

  constructor(private userApiService: UserApiService, private router: Router) {}

  ngOnInit(): void {
    this.userApiService.getAllUser().subscribe({
      next: (data) => {
        this.users = data.data.users;
        console.log('user', this.users);
      },
      error: (error) => {
        console.error('Error fetching user1:', error);
      },
    });
  }

  users: any[] = [];

  searchName = '';
  searchEmail = '';
  searchRole = '';
  totalUser = 0;

  isModalOpen = false;
  modalType: 'add' | 'edit' | 'delete' | null = null;
  selectedUser: any = {};

  // Phân trang
  currentPage = 1;
  pageSize = 10;

  get totalPages() {
    return Math.ceil(this.filteredUsers().length / this.pageSize);
  }

  filteredUsers() {
    const listFilter = this.users.filter(user => {
      const nameMatch = user.name.toLowerCase().includes(this.searchName.toLowerCase());
      const emailMatch = user.email.toLowerCase().includes(this.searchEmail.toLowerCase());
      const roleMatch = this.searchRole ? user.role === this.searchRole : true;
      return nameMatch && emailMatch && roleMatch;
    });
    this.totalUser = listFilter.length;
    return listFilter;
  }

  paginatedUsers() {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.filteredUsers().slice(start, start + this.pageSize);
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

  openModal(type: 'add' | 'edit' | 'delete', user: any = null) {
    console.log(user)
    this.modalType = type;
    this.isModalOpen = true;
    this.selectedUser = user ? { ...user } : { id: this.users.length + 1, name: '', email: '', role: 'user' };
  }

  closeModal() {
    this.isModalOpen = false;
    this.selectedUser = {};
  }

  addUser() {
    this.users.push(this.selectedUser);
    this.closeModal();
  }

  updateUser() {
    const index = this.users.findIndex(u => u.id === this.selectedUser.id);
    if (index !== -1) this.users[index] = this.selectedUser;
    console.log(this.users[index])
    this.closeModal();
  }

  deleteUser() {
    this.users = this.users.filter(u => u.id !== this.selectedUser.id);
    this.closeModal();
  }

  toLinkDoctors(){
    this.router.navigate(['/admin/doctors']);
  }
}
