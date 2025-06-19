import { Component } from '@angular/core';
import { ToastService } from '@app/services/toast/toast.service';
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { FeedbackApiService } from '@app/services/api/feedback/feedback.api.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent {

  private isLogin = false;

  constructor(
      private toastService: ToastService,
      private authService: AuthApiService,
      private feedbackApiService: FeedbackApiService,
    ) {}




  ngOnInit(): void {
    this.getAllLimit();

  }
  filteredReviews: any[] = [];


  searchTitle = '';
  searchDoctor = '';
  sortOption = 'newest';
  showForm = false;

  newReview = {
    title: '',
    doctor: '',
    comment: '',
    rating: 5,
    date: ''
  };

  openReviewForm() {
    this.showForm = true;
    this.newReview = { title: '', doctor: '', comment: '', rating: 5, date: '' };
  }

  closeReviewForm() {
    this.showForm = false;
  }

  submitReview() {
    this.newReview.date = new Date().toISOString().split('T')[0];
    console.log(this.newReview)
    this.checkIsLogin();
    if(this.isLogin){
      if(this.newReview.rating > 5){
        this.toastService.warning('Số sao đánh giá từ 1 - 5.');
        return;
      }

      this.feedbackApiService.createFeedback(this.newReview).subscribe({
        next: (data) => {
          this.toastService.success('Thêm đánh giá thành công.');
          this.isLogin = false;
          this.getAllLimit();
          this.closeReviewForm();
        },
        error: (error) => {
          this.toastService.error(error.error.message);
        },
      });
    }
  }

  applyFilters() {
    this.filteredReviews = this.filteredReviews.filter(r =>
      r.title.toLowerCase().includes(this.searchTitle.toLowerCase()) &&
      r.doctor.toLowerCase().includes(this.searchDoctor.toLowerCase())
    );
    this.sortReviews();
  }

  sortReviews() {
    switch (this.sortOption) {
      case 'newest':
        this.filteredReviews.sort((a, b) => b.date.localeCompare(a.date));
        break;
      case 'oldest':
        this.filteredReviews.sort((a, b) => a.date.localeCompare(b.date));
        break;
      case 'highest':
        this.filteredReviews.sort((a, b) => b.rating - a.rating);
        break;
      case 'lowest':
        this.filteredReviews.sort((a, b) => a.rating - b.rating);
        break;
    }
  }

  checkIsLogin(){
    this.authService.requiredRolePatient().subscribe({
      next: (response) => {
        this.isLogin = true;
      },
      error: (error) => {
        this.toastService.warning('Bạn chưa đăng nhập.');
        this.isLogin = false;
      }
    });
  }

  getAllLimit(){
    this.feedbackApiService.getAllLimit(1000).subscribe({
      next: (data) => {
        this.filteredReviews = data.data.feedback;
        this.applyFilters();
        console.log(data.data.feedback)
      },
      error: (error) => {
        console.log(error)
      },
    });
  }
}
