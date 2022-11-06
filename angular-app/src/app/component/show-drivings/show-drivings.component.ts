import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from 'src/app/service/config.service';
import { Router } from '@angular/router'; 
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import { MatDialogConfig } from '@angular/material/dialog';
import { RatingDialogComponent } from '../rating-dialog/rating-dialog.component';

@Component({
  selector: 'app-show-drivings',
  templateUrl: './show-drivings.component.html',
  styleUrls: ['./show-drivings.component.css']
})
export class ShowDrivingsComponent implements OnInit {

  constructor( private http: HttpClient, private configService: ConfigService, private router: Router, public dialog: MatDialog) { }

  drivings = [];
  
  ngOnInit(): void {
    console.log("component has been initialized!")
   this.http.get(this.configService.drivings_url + '/ana@gmail.com').subscribe((response:any) => {
      this.drivings = response;
      console.log(response[response.length-1])
   }
   )
   }

   sortByPrice(){
    const sorted = this.drivings.sort(
      (objA, objB) => objA.price - objB.price,
    );
    console.log(sorted);
   }

   sortByDate(){
    const sorted = this.drivings.sort(
      (objA, objB) => objA.price - objB.price,
    );
    console.log(sorted);
   }


   goToDetailsPage(id: number){
    console.log(id);
    this.router.navigate(["/details", id]);
   }

   isDisabledBtnRate(date) : boolean{
    const date_today:Date = new Date();
    console.log(this.getDifferenceInDays(Date.parse(date), date_today));
    if(this.getDifferenceInDays(date, date_today) > 3){
      return true;
    }
    else{
      return false;
    }
   }

   getDifferenceInDays(date1, date2):number {
    const diffInMs = Math.abs(date2 - Date.parse(date1));
    return diffInMs / (1000 * 60 * 60 * 24);
  }

   openDialog(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;



    const dialogRef = this.dialog.open(RatingDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => console.log("Dialog output:", data)
  );    
  }
   }


