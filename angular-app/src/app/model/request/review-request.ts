export class ReviewRequest{
    vehicleRate: number;
    driverRate: number;
    message: string;
    driving: number;

    constructor(vehicleRate: number, driverRate: number, message: string, driving: number){
        this.vehicleRate = vehicleRate;
        this.driverRate = driverRate;
        this.message = message;
        this.driving = driving;
    }
}