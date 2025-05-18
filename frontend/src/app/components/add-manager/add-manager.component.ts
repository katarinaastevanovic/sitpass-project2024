import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from '../../model/user';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-add-manager',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule], // Ovde dodaješ FormsModule
  templateUrl: './add-manager.component.html',
  styleUrls: ['./add-manager.component.css']
})
export class AddManagerComponent implements OnInit {
  users: User[] = [];
  selectedUserId: number | null = null;
  facilityId: number | null = null;

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Provera tokena i role
    const token = localStorage.getItem('accessToken');
    console.log('AccessToken:', token);  // Ispis tokena
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiry = payload.exp * 1000; // Konverzija u milisekunde
      console.log('Token payload:', payload);  // Ispis payloada tokena

      if (Date.now() >= expiry) {
        console.error('Token je istekao');
        // Opcionalno preusmeri korisnika na login ili osveži token
      } else if (payload.role === 'ROLE_ADMIN') {
        console.log('Korisnik je admin');
      } else {
        console.log('Korisnik nije admin');
      }
    } else {
      console.error('Token nije pronađen u localStorage');  // Ako token nije prisutan
    }

    this.getUsers();
    this.facilityId = Number(this.route.snapshot.paramMap.get('id'));
    console.log('Facility ID:', this.facilityId);  // Ispis Facility ID
  }

  getUsers() {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });
    
    this.http.get<User[]>('http://localhost:8080/api/v1/users', { headers }).subscribe(
      response => {
        this.users = response;
        console.log('Korisnici preuzeti:', this.users);  // Ispis preuzetih korisnika
      },
      error => {
        console.error('Greška prilikom preuzimanja korisnika:', error);  // Ispis greške
        if (error.status === 403) {
          console.error('Pristup zabranjen: Možda nemate potrebne dozvole.');
        }
      }
    );
  }

  addManager() {
    // Preuzimanje facility ID-a iz URL-a
    const facilityId = Number(this.route.snapshot.paramMap.get('id'));
    console.log('Odabrani User ID:', this.selectedUserId);  // Ispis odabranog User ID-a
    console.log('Facility ID:', facilityId);  // Ispis Facility ID-a
  
    if (this.selectedUserId && facilityId) {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
      });
  
      console.log('Zaglavlja:', headers);  // Ispis zaglavlja za zahtev
  
      this.http.post(`http://localhost:8080/api/facilities/${facilityId}/add-manager?userId=${this.selectedUserId}`, {}, { headers })
      .subscribe(
          () => {
            console.log('Menadžer uspešno dodat');
            this.router.navigate(['/facilities']);
          },
          error => {
            console.error('Greška prilikom dodavanja menadžera:', error);  // Ispis greške
            this.handleError(error);
          }
        );
    } else {
      console.error('User ID ili Facility ID nedostaje');
    }
  }
  

  handleError(error: any): void {
    if (error.status === 403) {
      window.alert('Nemate dozvolu za ovu akciju.');
    } else {
      window.alert('Došlo je do greške prilikom dodavanja menadžera.');
    }
  }
}
