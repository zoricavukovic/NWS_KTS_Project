export class Location{
    city: string = "";
    street: string = "";
    number: number = -1;
    lon: number = -1;
    lat: number = -1;
    constructor();

    constructor(
      city?: string,
      street?: string,
      number?: number,
      lon?: number,
      lat?: number
    ){
     this.city = city;
     this.street = street;
     this.number = number;
     this.lon = lon;
     this.lat = lat;
    }
}
