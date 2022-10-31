export class VehicleRequest {
    petFriendly: boolean;
    babySeat: boolean;
    vehicleType: string;

    constructor(
        petFriendly: boolean,
        babySeat: boolean,
        vehicleType: string
    ){
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
        this.vehicleType = vehicleType;
    }
}