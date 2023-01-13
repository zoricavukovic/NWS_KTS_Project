import { SimpleDrivingInfo } from "../models/driving/simple-driving-info";
import {DrivingNotification} from "../models/notification/driving-notification";

export class AddDrivingNotification {
    static readonly type = "[DrivingNotification] Add";

    constructor(public payload: DrivingNotification) {}
  }

  export class GetDrivingNotification {
    static readonly type = "[DrivingNotification] Get";
  }

  export class UpdateMinutesStatusDrivingNotification {
    static readonly type = "[DrivingNotification] UpdateMinutesStatus";

    constructor(public payload: {minutes: number, drivingStatus: string}) {}
  }

  export class UpdateDrivingNotification {
    static readonly type = "[DrivingNotification] UpdateDriving";

    constructor(public payload: SimpleDrivingInfo) {}
  }

  export class UpdateStatusDrivingNotification {
    static readonly type = "[DrivingNotification] UpdateStatus";

    constructor(public payload: {active: boolean, drivingStatus: string}) {}
  }
