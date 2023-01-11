export class DrivingNotificationRequest {
  lonStarted: number;
  latStarted: number;
  lonEnd: number;
  latEnd: number;
  senderEmail: string;
  price: number;
  passengers: string[];
  constructor(
    lonStarted: number,
    latStarted: number,
    lonEnd: number,
    latEnd: number,
    senderEmail: string,
    price: number,
    passengers: string[]
  ) {
    this.lonStarted = lonStarted;
    this.latStarted = latStarted;
    this.lonEnd = lonEnd;
    this.latEnd = latEnd;
    this.senderEmail = senderEmail;
    this.price = price;
    this.passengers = passengers;
  }
}
