import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { CommonModule, NgIf } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-account-request',
  templateUrl: './account-request.component.html',
  styleUrls: ['./account-request.component.css'],
  imports: [ReactiveFormsModule, CommonModule, NgIf, HttpClientModule]
})
export class AccountRequestComponent implements OnInit {
  accountRequests: any[] = [];
  errorMessage: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadAccountRequests();
  }

  loadAccountRequests(): void {
    const token = localStorage.getItem('accessToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });

    this.http.get<any[]>('http://localhost:8080/api/v1/users/requests', { headers })
      .subscribe({
        next: (data) => {
          this.accountRequests = data;
        },
        error: (error) => {
          this.errorMessage = 'Failed to load account requests.';
        }
      });
  }

  approveRequest(requestId: number): void {
    const token = localStorage.getItem('accessToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });

    this.http.post(`http://localhost:8080/api/v1/users/approve/${requestId}`, {}, { headers })
      .subscribe({
        next: (data) => {
          this.accountRequests = this.accountRequests.filter(request => request.id !== requestId); // Uklanjanje odbijenog zahteva
          this.loadAccountRequests();
        },
        error: (error) => {
          this.errorMessage = 'Failed to reject request.';
        }
      });

  }

  rejectRequest(requestId: number): void {
    const token = localStorage.getItem('accessToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });

    this.http.post(`http://localhost:8080/api/v1/users/reject/${requestId}`, {}, { headers })
      .subscribe({
        next: (data) => {
          this.accountRequests = this.accountRequests.filter(request => request.id !== requestId); // Uklanjanje odbijenog zahteva
          this.loadAccountRequests();
        },
        error: (error) => {
          this.errorMessage = 'Failed to reject request.';
        }
      });
  }
}
