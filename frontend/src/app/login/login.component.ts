import { Component, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { AuthenticationService } from '../service/authentication.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmailValidator } from '../validators/email-validator';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  signInForm!: FormGroup;
  registerForm! : FormGroup;

  showAlert: boolean = false;

  constructor(private authService: AuthenticationService, private router: Router, private emailValidator: EmailValidator) { }

  ngOnInit() {
    this.signInForm = new FormGroup({
      email: new FormControl(null, [Validators.required, Validators.email]),
      password: new FormControl(null, [Validators.required],),
    });

    this.registerForm = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      email: new FormControl(null, [Validators.required, Validators.email], this.emailValidator.validate.bind(this.emailValidator)),
      password: new FormControl(null, [Validators.required]),
    });
  }

  onSubmitSignIn() {
    if (this.signInForm !== null || this.signInForm !== undefined) {
      this.authService.login(this.signInForm!.value.email, this.signInForm!.value.password).subscribe(
        {
          next: (response) => {
            this.signInForm.reset();
            this.router.navigate(["/Home"]);;
          },
          error: (error: HttpErrorResponse) => {
            this.signInForm.reset();
            this.handleError(error, 'Error in authenticate user')
          }
        }
      );
    }
  }

  onSubmitRegister() {
    this.authService.register(this.registerForm!.value).subscribe(
      {
        next: (response) => {
          this.registerForm!.reset();;
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(error, 'Error in register user')
        }
      }
    );
  }

  handleError(error: HttpErrorResponse, message: string) {
    if (error.status === 401 || error.status === 403) {
      this.showAlert = true;
    }
  }

}
