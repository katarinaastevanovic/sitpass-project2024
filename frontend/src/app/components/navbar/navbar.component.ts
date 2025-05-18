import { Component, OnInit } from '@angular/core';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common'; // Importuj CommonModule za *ngIf
import { filter } from 'rxjs/operators';

@Component({
  standalone: true,
  imports: [RouterModule, CommonModule], // Uključi CommonModule za *ngIf
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;
  isAdmin = false;

  constructor(private router: Router) {
    this.checkLoginStatus();

    // Pretplata na događaje rutera da bi se status prijave ažurirao pri promeni rute
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.checkLoginStatus(); // Proveravamo status logina i role pri svakoj promeni rute
    });
  }

  ngOnInit(): void {
    // Nema potrebe za logikom u ngOnInit jer sve ide kroz checkLoginStatus
  }

  checkLoginStatus(): void {
    // Provera da li je korisnik ulogovan
    this.isLoggedIn = !!localStorage.getItem('accessToken');

    // Provera uloge korisnika (admin ili ne)
    const role = localStorage.getItem('role');
    this.isAdmin = role === 'ROLE_ADMIN'; // Ako je role ROLE_ADMIN, postavi isAdmin na true

    console.log('Login status:', this.isLoggedIn);
    console.log('Admin status:', this.isAdmin); // Dodaj za debug
  }

  public logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    this.isLoggedIn = false; 
    this.isAdmin = false;  // Resetujemo admin status pri logoutu
    this.router.navigate(['/login']);
  }
}
