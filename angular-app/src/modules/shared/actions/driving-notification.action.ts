import { Driving } from "../models/driving/driving";
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

    constructor(public payload: {minutes: number, drivingStatus: string, drivingId: number}) {}
  }

  export class UpdateOnlyMinutesStatus {
    static readonly type = "[DrivingNotification] UpdateOnlyMinutesStatus";

    constructor(public payload: {minutes: number}) {}
  }

  export class UpdateDrivingNotification {
    static readonly type = "[DrivingNotification] UpdateDriving";

    constructor(public payload: SimpleDrivingInfo) {}
  }

  export class UpdateStatusDrivingNotification {
    static readonly type = "[DrivingNotification] UpdateStatus";

    constructor(public payload: {active: boolean, drivingStatus: string}) {}
  }

  export class ClearStore {
    static readonly type = "[DrivingNotification] ClearDriving";
  }

  export class AddDrivings {
    static readonly type = "[Drivings] AddDrivings";

    constructor(public payload: Driving[]) {}
  }

export class GetDrivings {
    static readonly type = "[Drivings] GetDrivings";
}

export class UpdateDrivings {
    static readonly type = "[Drivings] UpdateDrivings";

    constructor(public payload: Driving) {}
}


