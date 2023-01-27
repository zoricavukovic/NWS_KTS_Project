import { Route } from '../models/route/route';
import { Driving } from '../models/driving/driving';
import { SimpleDrivingInfo } from '../models/driving/simple-driving-info';
import { DrivingNotification } from '../models/notification/driving-notification';

export class AddDrivingNotification {
  static readonly type = '[DrivingNotification] Add';

  constructor(public payload: DrivingNotification) {}
}

export class GetDrivingNotification {
  static readonly type = '[DrivingNotification] Get';
}

export class UpdateMinutesStatusDrivingNotification {
  static readonly type = '[DrivingNotification] UpdateMinutesStatus';

  constructor(
    public payload: {
      minutes: number;
      drivingStatus: string;
      drivingId: number;
    }
  ) {}
}

export class UpdateOnlyMinutesStatus {
  static readonly type = '[DrivingNotification] UpdateOnlyMinutesStatus';

  constructor(public payload: { minutes: number }) {}
}

export class UpdateDrivingNotification {
  static readonly type = '[DrivingNotification] UpdateDriving';

  constructor(public payload: SimpleDrivingInfo) {}
}

export class SimpleUpdateDrivingNotification {
  static readonly type = '[DrivingNotification] SimpleUpdateDriving';

  constructor(
    public payload: {
      route: Route;
      vehicleType: string;
      drivingStatus: string;
      active: boolean;
      vehicleId: number;
      drivingId: number;
      minutes: number;
    }
  ) {}
}

export class UpdateStatusDrivingNotification {
  static readonly type = '[DrivingNotification] UpdateStatus';

  constructor(
    public payload: { active: boolean; drivingStatus: string; started?: Date }
  ) {}
}

export class ClearStore {
  static readonly type = '[DrivingNotification] ClearDriving';
}

export class UpdateIdDrivingNotification {
  static readonly type = '[DrivingNotification] UpdateIdDrivingNotification';

  constructor(public payload: { drivingId: number }) {}
}

export class UpdateDurationDrivingNotification {
  static readonly type =
    '[DrivingNotification] UpdateDurationDrivingNotification';

  constructor(public payload: { duration: number }) {}
}

export class UpdateIfDriverChooseWrongRoute {
  static readonly type = '[DrivingNotification] UpdateIfDriverChooseWrongRoute';

  constructor(public payload: boolean) {}
}

export class ResetVehicleInDrivingNotification {
  static readonly type =
    '[DrivingNotification] ResetVehicleInDrivingNotification';
}

export class RemoveDriving {
  static readonly type = '[Drivings] RemoveDriving';
  constructor(public payload: number) {}
}

export class AddDrivings {
  static readonly type = '[Drivings] AddDrivings';

  constructor(public payload: Driving[]) {}
}

export class GetDrivings {
  static readonly type = '[Drivings] GetDrivings';
}

export class UpdateDrivings {
  static readonly type = '[Drivings] UpdateDrivings';

  constructor(public payload: Driving) {}
}
