import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './user/home/home.component';
import { LoginComponent } from './user/login/login.component';
import { HeaderComponent } from './user/shared/header/header.component';
import { FooterComponent } from './user/shared/footer/footer.component';
import { DiagnosisComponent } from './user/diagnosis/diagnosis.component';
import { DoctorComponent } from './user/doctor/doctor.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    HeaderComponent,
    FooterComponent,
    DiagnosisComponent,
    DoctorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
