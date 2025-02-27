import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './user/home/home.component'
import { LoginComponent } from './user/login/login.component'
import { DiagnosisComponent } from './user/diagnosis/diagnosis.component'
import { DoctorComponent } from './user/doctor/doctor.component'

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'diagnosis', component: DiagnosisComponent },
  { path: 'doctor', component: DoctorComponent },
  { path: '**', redirectTo: 'home' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
