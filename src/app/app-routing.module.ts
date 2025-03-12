import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from '@app/user/home/home.component'
import { LoginComponent } from '@app/user/login/login.component'
import { DiagnosisComponent } from '@app/user/diagnosis/diagnosis.component'
import { DoctorComponent } from '@app/user/doctor/doctor.component'
import { ProfileComponent } from '@app/user/profile/profile.component'
import { AdminHomeComponent } from '@app/admin/home/home.component'
import { AdminUserComponent } from '@app/admin/user/user.component'
import { RegisterComponent } from '@app/user/register/register.component'

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'diagnosis', component: DiagnosisComponent },
  { path: 'doctor', component: DoctorComponent },
  { path: 'profile', component: ProfileComponent },


  { path: 'admin/home', component: AdminHomeComponent },
  { path: 'admin/users', component: AdminUserComponent },

  { path: '**', redirectTo: 'home' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
