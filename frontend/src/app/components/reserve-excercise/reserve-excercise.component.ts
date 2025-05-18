import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-reserve-excercise',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, HttpClientModule],
  templateUrl: './reserve-excercise.component.html',
  styleUrls: ['./reserve-excercise.component.css']
})
export class ReserveExcerciseComponent implements OnInit {
  reserveForm: FormGroup;
  facilityId!: number;
  userId!: number;  // Dodata promenljiva za korisnički ID

  constructor(
    private fb: FormBuilder, 
    private http: HttpClient, 
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.reserveForm = this.fb.group({
      fromDateTime: ['', Validators.required],
      duration: [60, Validators.required]
    });
  }

  ngOnInit(): void {
    // Preuzimanje facilityId iz parametara rute
    this.route.paramMap.subscribe(params => {
      const id = params.get('facilityId');
      if (id) {
        this.facilityId = +id;
      } else {
        alert('Invalid facility ID!');
      }
    });

    // Preuzimanje korisničkog ID-a iz localStorage
    const user = localStorage.getItem('user');
    this.userId = JSON.parse(user!).id;
  }

  onSubmit() {
    if (this.reserveForm.valid && this.facilityId !== undefined) {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
      });
  
      const excerciseData = {
        facilityId: this.facilityId,
        fromDateTime: this.reserveForm.value.fromDateTime,
        duration: this.reserveForm.value.duration
      };
  
      this.http.post('http://localhost:8080/api/facilities/reservations', excerciseData, { headers }).subscribe(
        response => {
          console.log('Reservation successful', response);
          alert('Reservation successful!');
          this.router.navigate(['/']);
        },
        error => {
          if (error.status === 400) {
            // Custom message based on the error response from the server
            if (error.error && error.error.message === 'Facility closed') {
              alert('The facility is not available during this time period.');
            } else {
              alert('Error creating reservation: The facility may be closed during the selected time.');
            }
          } else {
            console.error('Error creating reservation', error);
            alert('An unexpected error occurred. Please try again later.');
          }
        }
      );
    } else {
      alert('Please fill all required fields and make sure the facility ID is valid.');
    }
  }
  
  
}
