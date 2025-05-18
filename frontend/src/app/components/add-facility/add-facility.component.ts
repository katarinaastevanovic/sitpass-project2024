import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgIf } from '@angular/common';
import { Discipline } from '../../model/discipline';

@Component({
  selector: 'add-facility',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgIf, HttpClientModule],
  templateUrl: './add-facility.component.html',
  styleUrls: ['./add-facility.component.scss']
})
export class AddFacilityComponent implements OnInit {
  facilityForm: FormGroup;
  daysOfWeek = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
  facilityId: number | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.facilityForm = this.fb.group({
      id: [''],
      active: [true],
      name: ['', Validators.required],
      description: [''],
      address: ['', Validators.required],
      city: ['', Validators.required],
      createdAt: [new Date().toISOString(), Validators.required],
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
    const token = localStorage.getItem('accessToken'); // Uzimamo token iz localStorage
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    });
    
    this.http.get<any>(`http://localhost:8080/api/facilities/${id}`, { headers }).subscribe(facility => {
      this.facilityForm.patchValue({
        id: facility.id,
        active: facility.active,
        name: facility.name,
        description: facility.description,
        address: facility.address,
        city: facility.city,
        createdAt: facility.createdAt
      });
      if (facility.workDays) {
        this.setWorkDays(facility.workDays);
      }
      if (facility.disciplines) {
        this.setDisciplines(facility.disciplines);
      }
    });
  }

  setWorkDays(workDays: any[]): void {
    const workDaysFormArray = this.facilityForm.get('workDays') as FormArray;
    workDays.forEach(workDay => {
      const fromTime = `${workDay.fromTime[0].toString().padStart(2, '0')}:${workDay.fromTime[1].toString().padStart(2, '0')}`;
      const untilTime = `${workDay.untilTime[0].toString().padStart(2, '0')}:${workDay.untilTime[1].toString().padStart(2, '0')}`;
      const validFromDate = new Date(workDay.validFrom[0], workDay.validFrom[1] - 1, workDay.validFrom[2]);

      workDaysFormArray.push(this.fb.group({
        id: [workDay.id],
        day: [workDay.day, Validators.required],
        fromTime: [fromTime, Validators.required],
        untilTime: [untilTime, Validators.required],
        validFrom: [validFromDate.toISOString().split('T')[0], Validators.required]
      }));
    });
  }

  setDisciplines(disciplines: Discipline[]): void {
    const disciplinesFormArray = this.facilityForm.get('disciplines') as FormArray;
    disciplines.forEach(discipline => {
      console.log(discipline.name);
      disciplinesFormArray.push(this.fb.group({
        id: [discipline.id],
        name: [discipline.name, Validators.required]
      }));
    });
  }

  get workDays(): FormArray {
    return this.facilityForm.get('workDays') as FormArray;
  }

  addWorkDay(): void {
    if (this.workDays.length < 7) {
      this.workDays.push(this.fb.group({
        day: ['', Validators.required],
        fromTime: ['', Validators.required],
        untilTime: ['', Validators.required],
        validFrom: ['', Validators.required]
      }));
    }
  }

  removeWorkDay(index: number): void {
    this.workDays.removeAt(index);
  }

  getAvailableDays(index: number): string[] {
    const selectedDays = this.workDays.controls
      .map((control, idx) => (idx !== index ? control.get('day')?.value : null))
      .filter(day => day !== null) as string[];
    return this.daysOfWeek.filter(day => !selectedDays.includes(day));
  }

  get disciplines(): FormArray {
    return this.facilityForm.get('disciplines') as FormArray;
  }

  addDiscipline(): void {
    this.disciplines.push(this.fb.group({
      name: ['', Validators.required]
    }));
  }

  removeDiscipline(index: number): void {
    this.disciplines.removeAt(index);
  }

  onSubmit(): void {
    const token = localStorage.getItem('accessToken'); // Uzimamo token iz localStorage
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    });

    if (this.facilityForm.valid) {
      if (this.facilityId) {
        // Ažuriramo postojeći objekat
        this.http.put(`http://localhost:8080/api/facilities/${this.facilityId}`, this.facilityForm.value, { headers })
          .subscribe({
            next: (data) => {
              console.log('Facility updated:', data);
              this.router.navigate(['/']);
            },
            error: (error) => {
              this.handleError(error);
            }
          });
      } else {
        // Kreiramo novi objekat
        this.http.post(`http://localhost:8080/api/facilities`, this.facilityForm.value, { headers })
          .subscribe({
            next: (data) => {
              console.log('Facility created:', data);
              this.router.navigate(['/']);
            },
            error: (error) => {
              this.handleError(error);
            }
          });
      }
    }
  }

  handleError(error: any): void {
    if (error.status === 400 && error.error && error.error.message === 'Facility with this name already exists.') {
      window.alert('Facility with this name already exists.');
    } else {
      window.alert('An error occurred while creating/updating the facility.');
    }
  }
}
