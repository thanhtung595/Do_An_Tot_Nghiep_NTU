import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './user/home/home.component';
import { LoginComponent } from './user/login/login.component';
import { HeaderComponent } from './user/shared/header/header.component';
import { FooterComponent } from './user/shared/footer/footer.component';
import { DiagnosisComponent } from './user/diagnosis/diagnosis.component';
import { DoctorComponent } from './user/doctor/doctor.component';
import { ProfileComponent } from './user/profile/profile.component';
import { ServiceComponent } from './admin/service/service.component';
import { PatientsComponent } from './admin/patients/patients.component';
import { MedicinesComponent } from './admin/medicines/medicines.component';
import { RoomComponent } from './admin/room/room.component';
import { AdminUserComponent } from './admin/user/user.component';
import { DoctorAdminComponent } from '@app/admin/doctor/doctor.component';
import { SidebarComponent } from './admin/shared/sidebar/sidebar.component';
import { RegisterComponent } from './user/register/register.component';
import { AdminMedicalrecordsComponent } from './admin/medicalrecords/medicalrecords.component';
import { ForgotPasswordComponent } from './user/forgot-password/forgot-password.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    HeaderComponent,
    FooterComponent,
    DiagnosisComponent,
    DoctorComponent,
    ProfileComponent,
    ServiceComponent,
    PatientsComponent,
    MedicinesComponent,
    RoomComponent,
    AdminUserComponent,
    DoctorAdminComponent,
    SidebarComponent,
    RegisterComponent,
    AdminMedicalrecordsComponent,
    ForgotPasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    CommonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
