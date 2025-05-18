import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { response, Router } from 'express';
import { Facility } from '../../model/facilities';

@Component({
  selector: 'app-facility',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './facility.component.html',
  styleUrl: './facility.component.css'
})

export class FacilityComponent implements OnInit {

  listOfFacilities: Facility[]=[]
  ngOnInit(): void {
    this.getFacilities();
  }

  constructor(private http:HttpClient, private router:Router){

  }

  getFacilities() {
    this.http.get<Facility[]>("https://localhost:8080/api/facilities").subscribe(
      
      response => {
        
        this.listOfFacilities=response
        console.log(response)
      }
    )

  }

}
