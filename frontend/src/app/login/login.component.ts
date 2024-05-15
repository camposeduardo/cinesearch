import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthenticationService } from '../service/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  @ViewChild('sigInForm') signInForm: NgForm | undefined;
  @ViewChild('registerForm') registerForm: NgForm | undefined;

  constructor(private authService: AuthenticationService, private router: Router) {}

  onSubmitSignIn() {
    if (this.signInForm !== null || this.signInForm !== undefined) {
      this.authService.login(this.signInForm!.value.email, this.signInForm!.value.password).subscribe(
        response => {
          this.signInForm!.reset();
          this.router.navigate([""]);
        }
      );
    }

  }

  onSubmitRegister() {
    this.authService.register(this.registerForm!.value).subscribe(
      response => {
        this.registerForm!.reset();
      }
    );
  }
}
