import { Component } from '@angular/core';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class AdminUserComponent {
  users = Array.from({ length: 100 }, (_, i) => ({
    id: i + 1,
    name: `User ${i + 1}`,
    email: `user${i + 1}@gmail.com`,
    role: i % 2 === 0 ? 'admin' : 'user'
  }));

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
    this.closeModal();
  }

  deleteUser() {
    this.users = this.users.filter(u => u.id !== this.selectedUser.id);
    this.closeModal();
  }
}
