import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Piece} from '../model/piece.model';
import {SetModel} from '../model/set.model';
import {BuildSetModel} from '../model/build-set.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly addUserUrl: string = `${environment.baseUrl}api/user/sets/own`;
  private readonly getPiecesUrl: string = `${environment.baseUrl}api/user/piece/false`;
  private readonly getShoppingUrl: string = `${environment.baseUrl}api/user/shopping`;
  private readonly getUserSetsUrl: string = `${environment.baseUrl}api/user/sets`
  private readonly getUserBuildingPlansUrl: string = `${environment.baseUrl}api/user/building-plans`
  private readonly userLoginUrl: string = `${environment.baseUrl}api/user/login`

  constructor(private http: HttpClient) {
  }

  addUserSet(set: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(
      this.addUserUrl, {id: set, token: localStorage.getItem("token") as string});
  }

  getUserPieces(): Observable<Piece[]> {
    return this.http.get<Piece[]>(this.getPiecesUrl, {headers: new HttpHeaders().append("Authorization", localStorage.getItem("token") as string)});
  }

  getShoppingPieces(): Observable<Piece[]> {
    return this.http.get<Piece[]>(this.getShoppingUrl, {headers: new HttpHeaders().append("Authorization", localStorage.getItem("token") as string)});
  }

  getUserSets(): Observable<SetModel[]> {
    return this.http.get<SetModel[]>(this.getUserSetsUrl, {headers: new HttpHeaders().append("Authorization", localStorage.getItem("token") as string)});
  }

  getUserBuildingPlans(): Observable<BuildSetModel[]> {
    return this.http.get<BuildSetModel[]>(this.getUserBuildingPlansUrl, {headers: new HttpHeaders().append("Authorization", localStorage.getItem("token") as string)});
  }

  addBuildPlan(setId: string): Observable<{message: string}> {
    return this.http.post<{message: string}>(this.addBuildPlanUrl(setId), {token: localStorage.getItem("token") as string});
  }

  deleteBuildPlanStatus(modelId: string): void {
    this.http.delete(this.deleteBuildPlanStatusUrl(modelId), {headers: new HttpHeaders().append("Authorization", localStorage.getItem("token") as string)}).subscribe();
  }

  private addBuildPlanUrl(setId: string): string{
    return `${this.getUserBuildingPlansUrl}/${setId}`;
  }

  private deleteBuildPlanStatusUrl(setId: string): string{
    return `${this.getUserBuildingPlansUrl}/${setId}`;
  }

  loginUser(login: string, password: string): void {
    this.http.post<{token: string}>(this.userLoginUrl, {userName: login, password: password}).subscribe(response => {
      localStorage.setItem("token", response.token);
    });
  }
}
