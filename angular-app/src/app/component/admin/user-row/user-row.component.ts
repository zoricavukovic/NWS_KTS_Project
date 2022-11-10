import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/model/response/user/user';
import { ConfigService } from 'src/app/service/config.service';

@Component({
  selector: 'user-row',
  templateUrl: './user-row.component.html',
  styleUrls: ['./user-row.component.css']
})
export class UserRowComponent implements OnInit {

  @Input() user: User;
  @Input() index: number;

  constructor(private configService: ConfigService) { }

  ngOnInit(): void {
    console.log(this.user);
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix
  }

}
