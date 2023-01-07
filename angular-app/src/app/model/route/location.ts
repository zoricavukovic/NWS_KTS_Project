export interface Location {
  lon: number;
  lat: number;
  city?: string;
  street?: string;
  number?: string;
  zipCode?: string;
}

export interface LngLat {
  lonLat: number[];
}
