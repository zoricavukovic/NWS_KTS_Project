export interface VehicleTypeInfo {
  vehicleType: string;
  startPrice: string;
  numOfSeats: string;
  img?: string;
  index?: number;
}

export function getVehiclePhotoNameBasedOnType(vehicleType: string): string {
  switch (vehicleType) {
    case 'VAN':
      return '/assets/images/van.svg'
    case 'SUV':
      return '/assets/images/suv.svg';
    default:
      return '/assets/images/car.svg';
  }
}

export function getMyVehicle(vehicleType: string): string {
  switch (vehicleType) {
    case 'VAN':
      return '/assets/images/my_location_van.svg';
    case 'SUV':
      return '/assets/images/my_location_suv.svg';
    default:
      return '/assets/images/my_location_car.svg';
  }
}

export function getActiveVehiclePhotoNameBasedOnType(vehicleType: string): string {
  switch (vehicleType) {
    case 'VAN':
      return '/assets/images/active-van.svg'
    case 'SUV':
      return '/assets/images/active-suv.svg';
    default:
      return '/assets/images/active-car.svg';
  }
}
