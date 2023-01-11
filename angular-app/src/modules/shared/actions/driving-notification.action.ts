import {DrivingNotification} from "../models/notification/driving-notification";

export class AddDrivingNotification {
    static readonly type = "[DrivingNotification] Add";

    constructor(public payload: DrivingNotification) {}
  }

  export class GetDrivingNotification {
    static readonly type = "[DrivingNotification] Get";
  }

  export class UpdateDrivingNotification {
    static readonly type = "[DrivingNotification] Update";

    constructor(public payload: DrivingNotification) {}
  }
