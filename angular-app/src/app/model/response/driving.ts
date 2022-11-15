import { Route } from "./route";
import {User} from "./user/user";

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
    users: User[];
    price: number;
    hasReviewForUser: boolean;

}
