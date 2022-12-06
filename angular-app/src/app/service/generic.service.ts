import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HeadersService } from './headers.service';

export abstract class GenericService<T> {
  constructor(
    protected _http: HttpClient,
    protected actionUrl: string,
    private service: HeadersService
  ) {}

  getAll(): Observable<T[]> {
    return this._http.get(this.actionUrl, {
      headers: this.service.getHeader(),
    }) as Observable<T[]>;
  }

  get(id: number): Observable<T> {
    return this._http.get(`${this.actionUrl}/${id}`, {
      headers: this.service.getHeader(),
    }) as Observable<T>;
  }

  create(object: T): Observable<T> {
    return this._http.post<T>(this.actionUrl, object, {
      headers: this.service.getHeader(),
    });
  }

  update(object: T): Observable<T> {
    return this._http.put<T>(this.actionUrl, object, {
      headers: this.service.getHeader(),
    });
  }

  updatePerPath(id: number): Observable<T> {
    return this._http.put<T>(`${this.actionUrl}/${id}`, null, {
      headers: this.service.getHeader(),
    });
  }

  delete(id: number): Observable<boolean> {
    return this._http.delete<boolean>(`${this.actionUrl}/${id}`, {
      headers: this.service.getHeader(),
    });
  }
}
