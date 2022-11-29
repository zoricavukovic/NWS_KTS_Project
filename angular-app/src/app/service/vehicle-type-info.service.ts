import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { VehicleTypeInfo } from '../model/response/vehicle-type-info';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class VehicleTypeInfoService {}
