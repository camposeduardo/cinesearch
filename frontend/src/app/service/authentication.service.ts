import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map, of } from 'rxjs';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { User } from '../model/User';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private userSubject: BehaviorSubject<User> | undefined;
  public user: Observable<User> | undefined;

  constructor(private http: HttpClient, private handler:HttpBackend){
    this.http = new HttpClient(handler);
  }

  login(email: string, password: string) {
    return this.http.post<any>(`${environment.apiUrl}/login`, { email, password })
    .pipe(map(response => {
      this.userSubject?.next(response);
      const token = JSON.stringify(response).split(":")[1].replace(/^"|"$/, '').replace('"}', '');
      localStorage.setItem("token", token);
    }));
  }

  register(user: User) {
    return this.http.post<any>(`${environment.apiUrl}/register`, user).pipe(map(response => {
      this.userSubject?.next(response);
    }));
  }

  checkIfEmailExists(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${environment.apiUrl}/${email}`);
  }

  getEmail() {
    const token = localStorage.getItem("token");
    if (token) {
      let payloadB64: string | undefined = token!.split(".")[1];
      let payload = window!.atob(payloadB64);
      let payloadObj = JSON.parse(payload);
      let email = payloadObj.sub;
      return email;
    }
    return null;
  }

  getToken() {
    return localStorage.getItem("token");;
  }

}
