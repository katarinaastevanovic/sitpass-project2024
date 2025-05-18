import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './components/layout/layout.component';
import { FacilityComponent } from './components/facility/facility.component';

export const routes: Routes = [

    {
        path: '',
        component: LayoutComponent,
        children: [
            {path: 'facilities', component: FacilityComponent},
        ]
    }
];
@NgModule ({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule{}