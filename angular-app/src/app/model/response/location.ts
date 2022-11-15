export class Location{
    public city: string = "";
    public street: string = "";
    public number: number = -1;
    public lon: number = -1;
    public lat: number = -1;
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
