import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { LayoutComponent } from './components/layout/layout.component';
import { FacilitiesComponent } from './components/facilities/facilities.component';

export const routes: Routes = [

    {
        path: '',
        component: LayoutComponent,
        children: [
            {path:'facilities',component:FacilitiesComponent}
        ]
    }

];

@NgModule ({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule{}