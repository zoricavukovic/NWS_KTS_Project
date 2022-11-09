export class Review {
    id: number;
    vehicleRate: number;
    driverRate: number;
    message: string;
    driving: number;

    constructor(
        id: number,
        vehicleRate: number, 
        driverRate: number, 
        message: string, 
        driving: number
    ){
        this.id = id;
        this.vehicleRate = vehicleRate;
        this.driverRate = driverRate;
        this.message = message;
        this.driving = driving;
    }
}