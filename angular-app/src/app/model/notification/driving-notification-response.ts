export interface DrivingNotificationResponse {
  lonStarted: number;
  latStarted: number;
  lonEnd: number;
  latEnd: number;
  senderEmail: string;
  price: number;
  receiverEmail: string;
  read: boolean;
  drivingNotificationType: string;
  reason?: string;
}
