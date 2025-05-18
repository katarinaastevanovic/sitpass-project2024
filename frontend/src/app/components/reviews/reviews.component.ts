import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

import { DatePipe, NgFor, NgIf } from '@angular/common';
import { Review } from '../../model/review';

@Component({
  standalone: true,
  selector: 'app-reviews',
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.css'],
  imports: [HttpClientModule, NgFor,DatePipe, NgIf]
})
export class ReviewsComponent implements OnInit {
  reviews: Review[] = [];
  facilityId!: number;

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Uzmi ID objekta iz rute
    this.facilityId = +(this.route.snapshot.paramMap.get('facilityId') ?? 0);
    const userId = localStorage.getItem('userId');

    if (userId) {
      this.getReviewsForFacilityAndManager(+userId, this.facilityId);
    }
  }

  getReviewsForFacilityAndManager(userId: number, facilityId: number): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });

    this.http.get<Review[]>(`http://localhost:8080/api/facilities/manager/${userId}/facility/${facilityId}/reviews`, { headers }).subscribe(
      (response) => {
        this.reviews = response;
      },
      (error) => {
        console.error('Error fetching reviews:', error);
      }
    );
  }

  deleteReview(reviewId: number): void {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
    });

    this.http.delete(`http://localhost:8080/api/facilities/delete/${reviewId}`, { headers })
      .subscribe({
        next: () => {
          this.reviews = this.reviews.filter(review => review.id !== reviewId);
          alert('Review deleted successfully.');
        },
        error: (error) => {
          console.error('Error deleting review:', error);
          alert('Failed to delete review.');
        }
      });
  }
}
