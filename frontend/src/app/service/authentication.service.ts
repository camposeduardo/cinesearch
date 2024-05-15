import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/User';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private userSubject: BehaviorSubject<User> | undefined;
  public user: Observable<User> | undefined;

  private signInOption = true;

  constructor(private http: HttpClient){}

  login(email: string, password: string) {
    return this.http.post<any>(`${environment.apiUrl}/login`, { email, password })
    .pipe(map(response => {
      this.userSubject?.next(response);
      this.signInOption = false;
      localStorage.setItem("token", JSON.stringify(response));
    }));
  }

  register(user: User) {
    return this.http.post<any>(`${environment.apiUrl}/register`, user).pipe(map(response => {
      this.userSubject?.next(response);

    }));;
  }

  getEmail() {
    const token = localStorage.getItem("token");
    let payloadB64 = token!.split(".")[1];
    let payload = window.atob(payloadB64);
    let payloadObj = JSON.parse(payload);
    let email = payloadObj.sub;
    return email;
  }



}
