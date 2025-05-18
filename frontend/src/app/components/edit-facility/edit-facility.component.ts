import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-facility',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './edit-facility.component.html',
  styleUrls: ['./edit-facility.component.css']
})
export class EditFacilityComponent implements OnInit {
  editFacilityForm: FormGroup;
  facilityId: number | null = null;
  daysOfWeek = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.editFacilityForm = this.fb.group({
      id: [''],
      name: ['', Validators.required],
      description: [''],
      address: ['', Validators.required],
      city: ['', Validators.required],
      totalRating: [null, Validators.required],
      active: [true, Validators.required],
      workDays: this.fb.array([]),
      disciplines: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.facilityId = +id;
        this.loadFacility(this.facilityId);
      }
    });
  }

  loadFacility(id: number): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`, // Dodajemo Authorization token
    });

    this.http.get<any>(`http://localhost:8080/api/facilities/${id}`, { headers }).subscribe({
      next: (facility) => {
        this.editFacilityForm.patchValue({
          id: facility.id,
          name: facility.name,
          description: facility.description,
          address: facility.address,
          city: facility.city,
          totalRating: facility.totalRating,
          active: facility.active
        });

        if (facility.workDays) {
          this.setWorkDays(facility.workDays);
        }
        if (facility.disciplines) {
          this.setDisciplines(facility.disciplines);
        }
      },
      error: (error) => {
        console.error('Error loading facility data', error);
        this.errorMessage = 'An error occurred while loading the facility data.';
      }
    });
  }

  get workDays(): FormArray {
    return this.editFacilityForm.get('workDays') as FormArray;
  }

  addWorkDay(): void {
    this.workDays.push(this.fb.group({
      day: ['', Validators.required],
      fromTime: ['', Validators.required],
      untilTime: ['', Validators.required],
      validFrom: ['', Validators.required]
    }));
  }

  removeWorkDay(index: number): void {
    this.workDays.removeAt(index);
  }

  get disciplines(): FormArray {
    return this.editFacilityForm.get('disciplines') as FormArray;
  }

  addDiscipline(): void {
    this.disciplines.push(this.fb.group({
      name: ['', Validators.required]
    }));
  }

  removeDiscipline(index: number): void {
    this.disciplines.removeAt(index);
  }

  setWorkDays(workDays: any[]): void {
    const workDaysFormArray = this.editFacilityForm.get('workDays') as FormArray;
    workDays.forEach(workDay => {
      workDaysFormArray.push(this.fb.group({
        day: [workDay.day, Validators.required],
        fromTime: [workDay.fromTime, Validators.required],
        untilTime: [workDay.untilTime, Validators.required],
        validFrom: [workDay.validFrom, Validators.required]
      }));
    });
  }

  setDisciplines(disciplines: any[]): void {
    const disciplinesFormArray = this.editFacilityForm.get('disciplines') as FormArray;
    disciplines.forEach(discipline => {
      disciplinesFormArray.push(this.fb.group({
        name: [discipline.name, Validators.required]
      }));
    });
  }

  onSubmit(): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('accessToken')}`, // Dodajemo Authorization token
    });

    if (this.editFacilityForm.valid && this.facilityId) {
      this.http.put(`http://localhost:8080/api/facilities/${this.facilityId}`, this.editFacilityForm.value, { headers })
        .subscribe({
          next: (data) => {
            console.log('Facility updated:', data);
            this.router.navigate(['/']);
          },
          error: (error) => {
            console.error('Error updating facility', error);
            this.errorMessage = 'An error occurred while updating the facility.';
          }
        });
    }
  }
}
