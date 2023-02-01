import { Component, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'app-not-found-page',
  templateUrl: './not-found-page.component.html',
  styleUrls: ['./not-found-page.component.css']
})
export class NotFoundPageComponent implements OnInit, OnDestroy {

  ngOnInit(): void {
    document.getElementById("button-live-chat").style.visibility = 'hidden';
  }

  ngOnDestroy(): void{
    document.getElementById("button-live-chat").style.visibility = 'visible';
  }
}
