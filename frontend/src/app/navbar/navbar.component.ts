import { Component } from '@angular/core';
import { MovieService } from '../service/movie.service';
import { Router } from '@angular/router';
import { AuthenticationService } from '../service/authentication.service';
import { WatchlistService } from '../service/watchlist.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  titleToSearch: string = '';

  signInButton = true;

  constructor(private movieService: MovieService, private router: Router, private authService: AuthenticationService, private watchlistService: WatchlistService) {}


  searchMovies() {
    this.movieService.searchRelatedMovies(this.titleToSearch).subscribe(
      {
        next: (response) => {
          this.router.navigate(["/Search", this.titleToSearch]);
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(error, 'Error searching movies')
        }
      }
    );
  }

  ngDoCheck() {
    this.showSignButton();
  }

  showSignButton() {
    this.signInButton = !localStorage.getItem("token");
  }


  logout() {
    localStorage.clear();
    this.router.navigate(["/Home"]);
  }

  getWatchlist() {
    this.watchlistService.getAllMoviesInWatchlist().subscribe();
  }

  handleError(error: HttpErrorResponse, message: string) {
    if (error.status === 401 || error.status === 403) {
      this.router.navigate(['/SignIn']);
    }
  }

}
