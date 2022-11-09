import { Location } from "./location";

export class Route{
    id: number;
    startPoint: Location;
    destinations: Location[];
    kilometres: number;
}