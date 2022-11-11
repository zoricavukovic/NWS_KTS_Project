export class ReviewRequest{
    vehicleRate: number;
    driverRate: number;
    message: string;
    driving: number;
    userEmail: string;

    constructor(vehicleRate: number, driverRate: number, message: string, driving: number, userEmail: string){
        this.vehicleRate = vehicleRate;
        this.driverRate = driverRate;
        this.message = message;
        this.driving = driving;
        this.userEmail = userEmail;
    }
}