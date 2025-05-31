import { Component } from '@angular/core';
import { HeaderApiServiceService } from '@app/services/api_admin/header/header.api.service.service'

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  constructor(private headerApiService: HeaderApiServiceService
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
        window.location.href = '/home';
      }
    }
}

