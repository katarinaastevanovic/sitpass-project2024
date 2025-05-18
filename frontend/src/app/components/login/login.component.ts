import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { JwtUtilsService } from '../../service/jwt-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  loginError: string = ''; // Dodata promenljiva za čuvanje poruke greške

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private jwtService: JwtUtilsService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    this.loginError = ''; // Resetovanje greške pri novom pokušaju logina

    if (this.loginForm.valid) {
      let headers = new HttpHeaders({
        'Content-Type': 'application/json',
      });

      const loginData = this.loginForm.value;
      

      this.http.post('http://localhost:8080/api/v1/auth/login', loginData, { headers }).subscribe(
        (response: any) => {
          headers = headers.set('Authorization', `Bearer ${response.accessToken}`);
          localStorage.setItem('accessToken', response.accessToken);
          localStorage.setItem('email', loginData.email);
          localStorage.setItem('role', this.jwtService.getRole(response.accessToken)); 
          
          this.http.get<any>(`http://localhost:8080/api/v1/users/email/${loginData.email}`, { headers }).subscribe(
            (userResponse: any) => {
              console.log('User details:', userResponse);
              localStorage.setItem('userId', userResponse.id.toString());
              this.router.navigate(['/facilities']);
            },
            error => {
              console.error('Error fetching user details', error);
              this.loginError = 'Error fetching user details'; // Postavljanje poruke greške
            }
          );
        },
        error => {
          console.error('Error during login', error);
          this.loginError = 'Invalid email or password. Please try again.'; // Postavljanje poruke greške
        }
      );
    } else {
      this.loginError = 'Please fill all required fields correctly.'; // Postavljanje poruke greške za nevalidna polja
    }
  }
}
