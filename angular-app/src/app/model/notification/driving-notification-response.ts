export interface DrivingNotificationResponse {
  lonStarted: number;
  latStarted: number;
  lonEnd: number;
  latEnd: number;
  senderEmail: string;
  price: number;
  receiverEmail?: string;
  passengers?: string[];
  read?: boolean;
  drivingNotificationType?: string;
  started?: Date;
  duration?: number;
  petFriendly?: boolean;
  babySeat?: boolean;
  vehicleType?: string;
  reason?: string;
  id?: number;
}
