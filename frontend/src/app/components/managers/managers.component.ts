import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../model/user';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-managers',
  standalone: true,
  imports: [HttpClientModule, NgFor, NgIf],
  templateUrl: './managers.component.html',
  styleUrl: './managers.component.css'
})
export class ManagersComponent implements OnInit {
  managers: User[] = [];
  facilityId: number | null = null;

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Uzmi facilityId iz URL-a
    this.facilityId = Number(this.route.snapshot.paramMap.get('facilityId'));
    if (this.facilityId) {
      this.getManagers();
    }
  }

  getManagers(): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
    });

    this.http.get<User[]>(`http://localhost:8080/api/facilities/${this.facilityId}/managers`, { headers })
      .subscribe(response => {
        this.managers = response;
      }, error => {
        console.error('Error fetching managers', error);
      });
  }

  removeManager(userId: number | undefined): void {
    if (userId === undefined) {
      console.error('Manager ID is undefined');
      return;
    }
  
    if (confirm('Are you sure you want to remove this manager?')) {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
      });
  
      this.http.delete(`http://localhost:8080/api/facilities/${this.facilityId}/remove-manager/${userId}`, { headers })
        .subscribe(() => {
          this.managers = this.managers.filter(manager => manager.id !== userId);
        }, error => {
          console.error('Error removing manager', error);
        });
    }
  }
  
}
