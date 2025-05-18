import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Facility } from '../../model/facility';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-facility',
  standalone: true,
  imports: [CommonModule, HttpClientModule, RouterModule,FormsModule], // Dodajte HttpClientModule ovde
  templateUrl: './facility.component.html',
  styleUrls: ['./facility.component.css'] // ispravka sa 'styleUrl' na 'styleUrls'
})
export class FacilityComponent implements OnInit {
  isAdmin = false;
  isUser = false;
  listOfFacilities: Facility[] = [];
  searchCity: string = '';
searchDiscipline: string = '';
minRating: number | null = null;
maxRating: number | null = null;
searchWorkDay: string = '';
daysOfWeek = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
isManager = false;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getFacilities();
  
    // Provera uloge korisnika (admin ili menadžer)
    const role = localStorage.getItem('role');
    this.isAdmin = role === 'ROLE_ADMIN'; // Ako je role ROLE_ADMIN, postavi isAdmin na true
    this.isUser = role === 'ROLE_USER';
  
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.checkIfManager(+userId);  // Proveravamo da li je korisnik menadžer
    }
    
    console.log('Admin status:', this.isAdmin);
    console.log('User status:', this.isUser);
  }
  
  checkIfManager(userId: number): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });
    this.http.get<boolean>(`http://localhost:8080/api/facilities/${userId}/is-manager`, { headers }).subscribe(
      (isManager: boolean) => {
        this.isManager = isManager;
        console.log('Manager status:', this.isManager);
      },
      error => {
        console.error('Error checking manager status', error);
      }
    );
  }
  

  getFacilities() {
    const headers = new HttpHeaders({
      
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });
    this.http.get<Facility[]>('http://localhost:8080/api/facilities', { headers }).subscribe(
      response => {
        this.listOfFacilities = response;
        console.log(response);
      }
    );
  }
  deleteFacility(id: number) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });
  
    if (confirm('Are you sure you want to delete this facility?')) {
      this.http.delete(`http://localhost:8080/api/facilities/${id}`, { headers }).subscribe(
        () => {
          console.log(`Facility with id ${id} deleted successfully.`);
          // Uklonite obrisani objekat iz liste
          this.listOfFacilities = this.listOfFacilities.filter(facility => facility.id !== id);
        },
        error => {
          console.error('Error deleting facility', error);
        }
      );
    }
  }
  
  searchFacilitiesByCity() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });
    this.http.get<Facility[]>(`http://localhost:8080/api/facilities/search/byCity?city=${this.searchCity}`, { headers }).subscribe(
      response => {
        this.listOfFacilities = response;
        console.log(response);
      },
      error => {
        console.error('Error fetching facilities by city', error);
      }
    );
  }
  searchFacilitiesByDiscipline() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });
    this.http.get<Facility[]>(`http://localhost:8080/api/facilities/search/byDiscipline?discipline=${this.searchDiscipline}`, { headers }).subscribe(
      response => {
        this.listOfFacilities = response;
        console.log(response);
      },
      error => {
        console.error('Error fetching facilities by discipline', error);
      }
    );
  }
  
  searchFacilitiesByRating() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });
    this.http.get<Facility[]>(`http://localhost:8080/api/facilities/search/byRating?minRating=${this.minRating}&maxRating=${this.maxRating}`, { headers }).subscribe(
      response => {
        this.listOfFacilities = response;
        console.log(response);
      },
      error => {
        console.error('Error fetching facilities by rating range', error);
      }
    );
  }
  
  searchFacilitiesByWorkDay() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });
    this.http.get<Facility[]>(`http://localhost:8080/api/facilities/search/byWorkDay?workDay=${this.searchWorkDay}`, { headers }).subscribe(
      response => {
        this.listOfFacilities = response;
        console.log(response);
      },
      error => {
        console.error('Error fetching facilities by work day', error);
      }
    );
  }
  
}