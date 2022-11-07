import { Route } from "./route";

export class Driving {
    id: number;
    active: boolean;
    duration: number;
    started: Date;
    payingLimit: Date;
    route: Route;
    drivingStatus: string;
    driverEmail: string;
    usersPaid: {};
    price: number;

}
