import { Component } from '@angular/core';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent {
  reviews = [
    {
      title: 'Bác sĩ rất tận tâm',
      doctor: 'Nguyễn Văn A',
      comment: 'Khám chữa bệnh cẩn thận, tư vấn rõ ràng.',
      rating: 5,
      date: '2024-11-15'
    },
    {
      title: 'Thời gian chờ lâu',
      doctor: 'Trần Thị B',
      comment: 'Chờ khám hơn 1 tiếng mới tới lượt.',
      rating: 2,
      date: '2025-03-02'
    },
    {
      title: 'Dịch vụ tốt',
      doctor: 'Lê Văn C',
      comment: 'Nhanh chóng, sạch sẽ, chuyên nghiệp.',
      rating: 4,
      date: '2025-05-20'
    }
  ];

  filteredReviews = [...this.reviews];

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
    this.reviews.unshift({ ...this.newReview });
    this.applyFilters();
    this.closeReviewForm();
  }

  applyFilters() {
    this.filteredReviews = this.reviews.filter(r =>
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
}
