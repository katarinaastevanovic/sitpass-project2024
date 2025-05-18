import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { LayoutComponent } from './components/layout/layout.component';
import { FacilityComponent } from './components/facilities/facility.component'; // Provjerite da je ispravna putanja
import { AddFacilityComponent } from './components/add-facility/add-facility.component'; // Dodajte import za AddFacilityComponent
import { FacilityDetailComponent } from './components/facility/facility.component';
import { EditFacilityComponent } from './components/edit-facility/edit-facility.component';
import { ReserveExcerciseComponent } from './components/reserve-excercise/reserve-excercise.component';
import { RateFacilityComponent } from './components/rate-facility/rate-facility.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { EditUserComponent } from './components/edit-user/edit-user.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { AccountRequestComponent } from './components/account-request/account-request.component';
import { AddManagerComponent } from './components/add-manager/add-manager.component';
import { ManagersComponent } from './components/managers/managers.component';
import { FacilityCommentsComponent } from './components/comments/comments.component';
import { MyExcercisesComponent } from './components/my-excercises/my-excercises.component';
import { ReviewsComponent } from './components/reviews/reviews.component';

export const routes: Routes = [
    {
        path: '',
        component: LayoutComponent,
        children: [
            { path: '', redirectTo: 'facilities', pathMatch: 'full' }, // Dodajte default redirect ako želite da ide na facilities
            { path: 'facilities', component: FacilityComponent },
            { path: 'facility/:id', component: FacilityDetailComponent },
            { path: 'add-facility', component: AddFacilityComponent }, // Dodajte rutu za AddFacilityComponent
            { path: 'edit-facility/:id', component: EditFacilityComponent },
            { path: 'reserve-excercise/:facilityId', component: ReserveExcerciseComponent }, // Promijenjeno :id u :facilityId
            { path: 'rate-facility/:id', component: RateFacilityComponent },
            { path: 'login', component:LoginComponent },
            { path: 'register', component: RegisterComponent },
            { path: 'change-password', component: ChangePasswordComponent },
            { path: 'edit-user', component: EditUserComponent},
            { path: 'account-request', component: AccountRequestComponent},
            { path: 'add-manager/:id', component: AddManagerComponent },
            { path: 'facility/:facilityId/managers', component: ManagersComponent },
            { path: 'facility/:id/comments', component: FacilityCommentsComponent },
            { path: 'my-excercises', component: MyExcercisesComponent },
            { path: 'facility/:facilityId/reviews', component: ReviewsComponent },
            { path: '**', redirectTo: '/login' },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}
