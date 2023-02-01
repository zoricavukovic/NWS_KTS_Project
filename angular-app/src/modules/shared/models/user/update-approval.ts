export interface DriverUpdateApproval {
    id: number;
    userEmail: string;
    name: string;
    surname: string;
    phoneNumber: string;
    city: string;
    approved: boolean
    vehicleType: string;
    petFriendly: boolean;
    babySeat: boolean
}
