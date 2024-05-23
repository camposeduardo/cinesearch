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
          this.router.navigate(["Search", this.titleToSearch]);
        },
        error: (error: HttpErrorResponse) => {
          // Some logic here
        }
      }
    );
  }

  ngDoCheck() {
    this.showSignButton();
  }

  showSignButton() {
    if (localStorage.getItem("token") === null) {
      this.signInButton = true;
      return;
    }
    this.signInButton = false;
  }


  logout() {
    localStorage.clear();
    this.router.navigate([" "]);
  }

  getWatchlist() {
    this.watchlistService.getAllMoviesInWatchlist().subscribe();
  }

}
