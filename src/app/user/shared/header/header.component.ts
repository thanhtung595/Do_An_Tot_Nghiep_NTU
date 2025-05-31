import { Component } from '@angular/core';
import { HeaderApiServiceService } from '@app/services/api/header/header.api.service.service'
import { AuthApiService } from '@app/services/api/auth/auth.api.service';
import { Router } from '@angular/router';
import { clearAccessToken } from '@app/services/token/TokenService';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  constructor(private headerApiService: HeaderApiServiceService,
    private authApiService : AuthApiService,
    private router : Router
  ) { }

  headers : any[] = [];
  ngOnInit(): void {
    this.headerApiService.getHeader().subscribe({
      next: (data) => {
        this.headers = data?.data.header ?? [];
        console.log("headers",this.headers);
      },
      error: (error) => {
        console.error('Error fetching services:', error);
      }
    });
  }

  onClickLogout(data : any, event: Event){
    if(data.url == "logout"){
      event.preventDefault();
      clearAccessToken();
      window.location.href = '/home';
    }
  }
}
