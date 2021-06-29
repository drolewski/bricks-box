import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserSet} from '../model/user-set.model';

@Injectable({
  providedIn: 'root'
})
export class GetSetService {

  private readonly getSetUrl = `${environment.baseUrl}api/sets/user/set`

  constructor(private http: HttpClient) {
  }

  getSet(modelId: string, withShoppingList: boolean): Observable<UserSet> {
    return this.http.get<UserSet>(
      this.requestUrl(modelId, withShoppingList),
      {headers: new HttpHeaders().append("Authorization", environment.token)}
    );
  }

  requestUrl(modelId: string, withShoppingList: boolean): string {
    return `${this.getSetUrl}/${modelId}/${withShoppingList}`;
  }
}
