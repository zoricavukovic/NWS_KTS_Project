export interface DrivingStatusNotification {
  driverEmail: string;
  minutes: number;
  drivingStatus: string;
  reason: string;
  paid: boolean;
}