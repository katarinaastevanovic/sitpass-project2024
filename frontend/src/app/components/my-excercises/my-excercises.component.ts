import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { CommonModule, NgFor } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-my-excercises',
  imports:[HttpClientModule,NgFor,CommonModule],
  templateUrl: './my-excercises.component.html',
  styleUrls: ['./my-excercises.component.css']
})
export class MyExcercisesComponent implements OnInit {
  excercises: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadExcercises();
  }

  loadExcercises(): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });

    const userId = localStorage.getItem('userId');

    this.http.get<any[]>(`http://localhost:8080/api/facilities/history/${userId}`, { headers }).subscribe(
      (data) => {
        this.excercises = data;
      },
      (error) => {
        console.error('Error loading excercises', error);
      }
    );
  }
}
