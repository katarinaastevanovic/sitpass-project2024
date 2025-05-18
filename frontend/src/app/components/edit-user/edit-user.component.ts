import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  editUserForm: FormGroup;
  userId: string | null;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.editUserForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      name: ['', Validators.required],
      surname: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      address: ['', Validators.required],
      birthday: ['', Validators.required],
      zipCode: ['', Validators.required],
      city: ['', Validators.required]
    });

    this.userId = localStorage.getItem('userId'); // Uzimamo userId iz localStorage
    console.log('User ID:', this.userId); // Dodato za debagovanje
  }

  ngOnInit(): void {
    if (!this.userId) {
      console.error('No user ID found, redirecting to login');
      this.router.navigate(['/login']);
    } else {
      this.loadUserData();
    }
  }

  loadUserData(): void {
    const token = localStorage.getItem('accessToken'); // Uzimamo token iz localStorage

    if (token && this.userId) {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      console.log('Authorization header:', headers.get('Authorization')); // Dodato za debagovanje

      this.http.get<any>(`http://localhost:8080/api/v1/users/id/${this.userId}`, { headers }).subscribe(
        data => {
          this.editUserForm.patchValue(data);
        },
        error => {
          console.error('Error loading user data', error);
        }
      );
    } else {
      console.error('No token found, redirecting to login');
      this.router.navigate(['/login']); // Redirektuj na login ako nema tokena
    }
  }

  onSubmit(): void {
    if (this.editUserForm.valid) {
      const userData = this.editUserForm.value;
      const token = localStorage.getItem('accessToken'); // Uzimamo token iz localStorage
  
      if (token && this.userId) {
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  
        this.http.put(`http://localhost:8080/api/v1/users/update/${this.userId}`, userData, { headers, responseType: 'text' as 'json' }).subscribe(
          response => {
            console.log('User updated successfully', response);
            alert('User updated successfully!');
            this.router.navigate(['/']);
          },
          error => {
            console.error('Error updating user', error);
            alert('Error updating user: ' + error.message);
          }
        );
      } else {
        console.error('No token found, redirecting to login');
        this.router.navigate(['/login']); // Redirektuj na login ako nema tokena
      }
    } else {
      alert('Please fill all required fields correctly.');
    }
    
  }
}
