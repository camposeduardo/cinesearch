import { Injectable } from "@angular/core";
import { AsyncValidator, AbstractControl, ValidationErrors } from "@angular/forms";
import { Observable, map, catchError, of } from "rxjs";
import { AuthenticationService } from "../service/authentication.service";

@Injectable({providedIn: 'root'})
export class EmailValidator implements AsyncValidator {
  constructor(private authService: AuthenticationService) {}
  validate(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.authService.checkIfEmailExists(control.value).pipe(
      map((isTaken) => (isTaken ? {isTaken: true} : null)),
      catchError(() => of(null)),
    );
  }
}
