import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from '@app/user/home/home.component'
import { LoginComponent } from '@app/user/login/login.component'
import { DiagnosisComponent } from '@app/user/diagnosis/diagnosis.component'
import { DoctorComponent } from '@app/user/doctor/doctor.component'
import { ProfileComponent } from '@app/user/profile/profile.component'
import { AdminHomeComponent } from '@app/admin/home/home.component'
import { RegisterComponent } from '@app/user/register/register.component'
import { ForgotPasswordComponent } from '@app/user/forgot-password/forgot-password.component'
import { DoctorPatientHistoryComponent } from '@app/user/doctor/patient-history/patient-history.component'
import { DoctorEditMedicalRecordComponent } from '@app/user/doctor/edit-medical-record/edit-medical-record.component'
import { PaymentComponent } from './user/payment/payment.component';
import { InvoiceListComponent } from '@app/user/invoice/invoice-list.component';
import { NotificationListComponent } from '@app/user/notification/notification-list.component';
import { ReviewComponent } from '@app/user/rating/review.component';

import { AdminUserComponent } from '@app/admin/user/user.component'
import { DoctorAdminComponent } from '@app/admin/doctor/doctor.component';
import { MedicinesComponent } from '@app/admin/medicines/medicines.component';
import { AdminMedicalrecordsComponent } from './admin/medicalrecords/medicalrecords.component';

import { ErrorComponent } from '@app/error/error.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'diagnosis', component: DiagnosisComponent },
  { path: 'doctor', component: DoctorComponent },
  { path: 'doctor/patient-history', component: DoctorPatientHistoryComponent },
  { path: 'doctor/edit-medical-record/:id', component: DoctorEditMedicalRecordComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'invoice', component: InvoiceListComponent },
  { path: 'notification', component: NotificationListComponent },
  { path: 'feedback', component: ReviewComponent },
  { path: 'error/:code', component: ErrorComponent },

  { path: 'admin/home', component: AdminHomeComponent },
  { path: 'admin/users', component: AdminUserComponent },
  { path: 'admin/doctors', component: DoctorAdminComponent },
  { path: 'admin/medicines', component: MedicinesComponent },
  { path: 'admin/medicalrecords', component: AdminMedicalrecordsComponent },

  {
    path: 'payment/:id',
    component: PaymentComponent
  },

  { path: '**', redirectTo: 'home' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
