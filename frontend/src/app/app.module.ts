import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { WatchlistComponent } from './watchlist/watchlist.component';
import { HomeComponent } from './home/home.component';
import { SearchComponent } from './search/search.component';
import { RouterModule, Routes } from '@angular/router';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { AlertComponent } from './alert/alert.component';
import { WatchlistService } from './service/watchlist.service';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { signInGuard } from './guards/sign-in.guard';

const appRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'Home', component: HomeComponent},
  {path: 'SignIn', component: LoginComponent, canActivate:[signInGuard]},
  {path: 'Watchlist', component: WatchlistComponent},
  {path: 'Search/:title', component: SearchComponent},
]


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    WatchlistComponent,
    HomeComponent,
    SearchComponent,
    AlertComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    ReactiveFormsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
    , WatchlistService],

  bootstrap: [AppComponent]
})
export class AppModule { }
