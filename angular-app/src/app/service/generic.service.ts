import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export abstract class GenericService<T> {
  constructor(
    protected _http: HttpClient,
    protected actionUrl: string
  ) {}

  getAll(): Observable<T[]> {

    return this._http.get(this.actionUrl) as Observable<T[]>;
  }

  get(id: number): Observable<T> {

    return this._http.get(`${this.actionUrl}/${id}`) as Observable<T>;
  }

  create(object: T): Observable<T> {

    return this._http.post<T>(this.actionUrl, object);
  }

  update(object: T): Observable<T> {
    return this._http.put<T>(this.actionUrl, object);
  }

  delete(id: number): Observable<boolean> {

    return this._http.delete<boolean>(`${this.actionUrl}/${id}`);
  }
}
