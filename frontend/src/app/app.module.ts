import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { WatchlistComponent } from './watchlist/watchlist.component';
import { HomeComponent } from './home/home.component';
import { SearchComponent } from './search/search.component';
import { RouterModule, Routes } from '@angular/router';

const appRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'Home', component: HomeComponent},
  {path: 'SignIn', component: LoginComponent},
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
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
