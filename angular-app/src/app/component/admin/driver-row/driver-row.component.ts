import { Component, OnInit, Input } from '@angular/core';
import { Driver } from 'src/app/model/response/user/driver';
import { ConfigService } from 'src/app/service/config.service';

@Component({
  selector: 'driver-row',
  templateUrl: './driver-row.component.html',
  styleUrls: ['./driver-row.component.css']
})
export class DriverRowComponent implements OnInit {

  @Input() driver: Driver;
  @Input() index: number;

  constructor(private configService: ConfigService) { }

  ngOnInit(): void {
    console.log(this.driver);
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix
  }

}
