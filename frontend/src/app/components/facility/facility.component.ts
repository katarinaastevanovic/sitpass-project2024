import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Facility } from '../../model/facility';

@Component({
    selector: 'app-facility-detail',
    standalone: true,
    imports: [CommonModule, HttpClientModule], // Dodajte CommonModule ovde
    templateUrl: './facility.component.html', // Koristimo templateUrl umesto inline templejta
    styleUrls: ['./facility.component.css']
})
export class FacilityDetailComponent implements OnInit {
    facility: Facility | undefined;

    constructor(private http: HttpClient, private route: ActivatedRoute) {}

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.getFacilityById(id);
        }
    }

    getFacilityById(id: string) {
        const headers = new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
          });
        this.http.get<Facility>(`http://localhost:8080/api/facilities/${id}`).subscribe(response => {
            this.facility = response;
        });
    }
}
