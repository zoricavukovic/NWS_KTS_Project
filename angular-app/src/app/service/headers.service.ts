import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class HeadersService {
  getHeader(): HttpHeaders {
    return new HttpHeaders().set(
      'Authorization',
      localStorage.getItem('token')
    );
  }
}
