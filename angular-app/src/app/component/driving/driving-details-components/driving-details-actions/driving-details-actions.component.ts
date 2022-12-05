import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-driving-details-actions',
  templateUrl: './driving-details-actions.component.html',
  styleUrls: ['./driving-details-actions.component.css'],
})
export class DrivingDetailsActionsComponent {
  @Input() favouriteRoute: boolean;
  @Output() setFavouriteRouteEvent = new EventEmitter<boolean>();

  setFavouriteRouteEmitter() {
    this.setFavouriteRouteEvent.emit(true);
  }
}
