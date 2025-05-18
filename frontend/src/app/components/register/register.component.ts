import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  emailExists = false;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    // Proveri da li email postoji kada se promeni polje
    this.registerForm.get('email')?.valueChanges
      .pipe(
        debounceTime(300), 
        distinctUntilChanged() 
      )
      .subscribe(value => this.checkEmailExists());
  }

  checkEmailExists() {
    const email = this.registerForm.get('email')?.value;
    if (email) {
      this.http.get<boolean>(`http://localhost:8080/api/v1/users/check-email?email=${email}`).subscribe(
        (exists) => {
          this.emailExists = exists;
        },
        (error) => {
          if (error.status === 403) {
            console.error('Access denied to check email endpoint');
          } else {
            console.error('Error checking email existence', error);
          }
        }
      );
    }
  }

  onSubmit() {
    if (this.registerForm.valid) {
      if (this.emailExists) {
        alert('Email already exists. Please use another email.');
        return;
      }

      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
      });

      const registerData = this.registerForm.value;

      this.http.post('http://localhost:8080/api/v1/users/register', registerData, { headers, responseType: 'text' as 'json' }).subscribe(
        response => {
          console.log('Registration successful', response);
          alert('Registration successful!');
          this.router.navigate(['/login']);
        },
        error => {
          if (error.status === 403) {
            console.error('Access denied to registration endpoint');
          } else {
            console.error('Error during registration', error);
            alert('Registration failed: ' + error.message);
          }
        }
      );
    } else {
      alert('Please fill all required fields correctly.');
    }
  }
}
