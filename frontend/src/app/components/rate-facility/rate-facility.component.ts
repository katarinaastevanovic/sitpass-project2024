import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-rate-facility',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './rate-facility.component.html',
  styleUrls: ['./rate-facility.component.css']
})
export class RateFacilityComponent implements OnInit {
  rateForm: FormGroup;
  facilityId!: number;
  ratings = Array.from({ length: 10 }, (_, i) => i + 1);
  userId!: number;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.rateForm = this.fb.group({
      equipment: [null, Validators.required],
      staff: [null, Validators.required],
      hygene: [null, Validators.required],
      space: [null, Validators.required],
      comment: [''] // Komentar nije obavezan, pa nema Validators.required
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.facilityId = +id;
      }
    });
    const user = localStorage.getItem('user');
    this.userId = JSON.parse(user!).id;
  }

  onSubmit() {
    if (this.rateForm.valid) {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
      });
  
      const rateData = {
        rate: {
          equipment: this.rateForm.value.equipment,
          staff: this.rateForm.value.staff,
          hygene: this.rateForm.value.hygene,
          space: this.rateForm.value.space
        },
        facilityId: this.facilityId,
        excerciseCount: null,
        hidden: false
      };
  
      // Prvo šaljemo ocenu
      this.http.post('http://localhost:8080/api/facilities/rate', rateData, { headers }).subscribe(
        (response: any) => {
          console.log('Rating successful', response);
  
          // Ako je komentar popunjen, šaljemo ga
          if (this.rateForm.value.comment) {
            const commentData = {
              text: this.rateForm.value.comment,
              reviewId: response.reviewId, // Pretpostavlja se da API vraća reviewId
              parentId: null // Ovo je glavni komentar, nije odgovor na drugi komentar
            };
  
            this.http.post('http://localhost:8080/api/comments', commentData, { headers }).subscribe(
              () => {
                console.log('Comment saved successfully');
              },
              error => {
                console.error('Error saving comment', error);
                alert('Error saving comment: ' + error.message);
              }
            );
          }
  
          alert('Rating successful!');
          this.router.navigate(['/']);
        },
        error => {
          console.error('Error submitting rating', error);
          if (error.status === 403) {
            alert('You do not have permission to perform this action.');
          } else {
            alert('Error submitting rating: ' + error.message);
          }
        }
      );
    } else {
      alert('Please fill all required fields.');
    }
  }
  
}
