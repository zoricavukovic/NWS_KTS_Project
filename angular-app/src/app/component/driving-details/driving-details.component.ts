import { HttpClient } from '@angular/common/http';
import { Driving } from 'src/app/model/driving';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConfigService } from 'src/app/service/config.service';

@Component({
  selector: 'app-driving-details',
  templateUrl: './driving-details.component.html',
  styleUrls: ['./driving-details.component.css']
})
export class DrivingDetailsComponent implements OnInit {

  id:string;
  driving:Driving;
  destinations: string[] = [];
  startPoint: string;
  val:number = 5;
  constructor( private route: ActivatedRoute, private http: HttpClient, private configService: ConfigService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
     
  
  
    this.http.get(this.configService.driving_details_url + this.id).subscribe((response:any) => {
      this.driving = response;
      this.startPoint = this.driving.route.startPoint.street + " " + this.driving.route.startPoint.number;
      this.destinations.push(this.startPoint);
      console.log(this.destinations)
      for (let destination of this.driving.route.destinations) {
        this.destinations.push(destination.street + " " + destination.number);
      }

   }
   )
  }

}
