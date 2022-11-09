export class VehicleTypeInfo {
    vehicleType: string;
    startPrice: string;
    numOfSeats: string;
    img?: string;
    index?: number;

    constructor(
        vehicleType: string,
        startPrice: string,
        numOfSeats: string
    ){
        this.vehicleType = vehicleType;
        this.startPrice = startPrice;
        this.numOfSeats = numOfSeats;
    }

}