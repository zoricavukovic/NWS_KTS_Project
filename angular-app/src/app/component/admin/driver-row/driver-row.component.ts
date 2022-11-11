import { Component, OnInit, Input } from '@angular/core';
import { Driver } from 'src/app/model/response/user/driver';
import { ConfigService } from 'src/app/service/config.service';
import { DrivingService } from 'src/app/service/driving.service';
import { Router } from '@angular/router';

@Component({
  selector: 'driver-row',
  templateUrl: './driver-row.component.html',
  styleUrls: ['./driver-row.component.css']
})
export class DriverRowComponent implements OnInit {

  @Input() driver: Driver;
  @Input() index: number;

  constructor(private configService: ConfigService, private drivingService: DrivingService, private router: Router) { }

  ngOnInit(): void {}
  

  viewDrivings(): void{
    
    this.drivingService.setUserEmail = this.driver.email;
    this.router.navigate(["/drivings"]);
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix
  }

}
