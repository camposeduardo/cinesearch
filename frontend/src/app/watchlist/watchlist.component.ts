import { Component, ElementRef, ViewChild } from '@angular/core';
import { WatchlistService } from '../service/watchlist.service';
import { MovieInfo } from '../model/MovieInfo';
import { AuthenticationService } from '../service/authentication.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent {

  @ViewChild('close') closeModal?: ElementRef;

  watchlistMovies: MovieInfo[] = [];
  movieInfo: MovieInfo | undefined;

  noPoster = '../../assets/no-poster.webp';

  constructor(private watchlistService: WatchlistService, private authService: AuthenticationService, private router: Router) { }

  ngOnInit() {
    window.addEventListener('closeModalEvent', this.closeModalListener.bind(this));
    if (this.authService.getToken() != null) {
      this.getAllMoviesInWatchlist();
    } else {
      this.router.navigate(["/SignIn"]);
    }
  }

  ngOnDestroy() {
    window.removeEventListener('closeModalEvent', this.closeModalListener.bind(this));
  }

  closeModalListener() {
    this.closeModal?.nativeElement.click();
  }

  getAllMoviesInWatchlist() {
    this.watchlistService.getAllMoviesInWatchlist().subscribe(
      {
        next: (data) => {
          this.watchlistMovies = data;
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(error, 'Error retrieving movies from watchlist');
        }
      }
    );
  }

  getMovieInformation(imdbId: string) {
    this.movieInfo = this.watchlistMovies?.find((movie) => movie.imdbId === imdbId);
  }

  onRemoveMovie(movieInfo: MovieInfo) {
    this.watchlistService.removeMovie(movieInfo).subscribe({
      next: (response) => {
        this.getAllMoviesInWatchlist();
        this.closeModalListener();
      },
      error: (error: HttpErrorResponse) => {
        this.handleError(error, 'Error on remove movie from watchlist');
      }
    });
  }

  handleError(error: HttpErrorResponse, message: string) {
    if (error.status === 401 || error.status === 403) {
      window.dispatchEvent(new CustomEvent('closeModalEvent'));
      this.router.navigate(['/SignIn']);
    }
  }


}
