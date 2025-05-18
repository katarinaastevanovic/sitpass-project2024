import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule, RouterModule],
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  changePasswordForm: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.changePasswordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmNewPassword: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.changePasswordForm.valid) {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
      });

      const changePasswordData = this.changePasswordForm.value;

      this.http.post(`http://localhost:8080/api/v1/users/change-password/${localStorage.getItem('userId')}`, changePasswordData, { headers, responseType: 'text' }).subscribe(
        () => {
          alert('Password changed successfully');
          this.router.navigate(['/']);
        },
        error => {
          console.error('Error changing password', error);
          this.errorMessage = 'Failed to change password. Please try again.';
        }
      );
    } else {
      this.errorMessage = 'Please fill all required fields correctly.';
    }
  }
}
