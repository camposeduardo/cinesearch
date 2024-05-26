import { Component, ElementRef, ViewChild } from '@angular/core';
import { MovieService } from '../service/movie.service';
import { Movie } from '../model/Movie';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieInfo } from '../model/MovieInfo';
import { WatchlistService } from '../service/watchlist.service';
import { AuthenticationService } from '../service/authentication.service';
import { map } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {

  @ViewChild('closeModal') closeModal!: ElementRef<HTMLButtonElement>;

  titleToSearch!: string;

  movieInWatchlist: boolean = false;

  moviesFounded: Movie[] | null = [];

  movieInfo: MovieInfo | undefined;

  noPoster = '../../assets/no-poster.webp';

  constructor(private movieService: MovieService, private watchlistService: WatchlistService,
    private authService: AuthenticationService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.getMovies();
    this.titleToSearch = "";
    window.addEventListener('closeModalEvent', this.closeModalListener.bind(this));
  }

  ngDoCheck() {
    this.titleToSearch = this.route.snapshot.params['title'];
  }

  ngOnDestroy() {
    window.removeEventListener('closeModalEvent', this.closeModalListener.bind(this));
  }

  closeModalListener() {
    this.closeModal?.nativeElement.click();
  }

  getMovies() {
    this.movieService.movies.subscribe(
      {
        next: (data) => {
          if (data != null) {
            this.moviesFounded = data;
          }
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(error, 'Error retrieving movies')
        }
      }
    );
  }

  getMovieInformation(imdbId: string) {
    this.movieService.searchMovieByImdbId(imdbId).subscribe({
      next: (response) => {
        if (response != null) {
          this.movieInfo = response;
          if (this.authService.getToken() != null) {
            this.verifyIfMovieIsInWatchlist(response);
          }

        }
      },
      error: (error: HttpErrorResponse) => {
        this.handleError(error, 'Error retrieving movie information')
      }
    }
    );
  }

  verifyIfMovieIsInWatchlist(movie: MovieInfo) {
    this.watchlistService.verifyIfMovieIsInWatchlist(movie).subscribe(
      {
        next: (response) => {
          this.movieInWatchlist = response;
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(error, 'Error checking watchlist')
        }
      }
    );
  }

  addToWatchlist(movie: MovieInfo) {
    this.watchlistService.addToWatchlist(movie).subscribe({
      next: () => this.closeModalListener(),
      error: (error: HttpErrorResponse) => this.handleError(error, 'Error adding to watchlist')
    });

  }

  onRemoveMovieFromWatchlistButton(movie: MovieInfo) {
    this.watchlistService.removeMovie(movie).subscribe({
      next: () => this.closeModalListener(),
      error: (error: HttpErrorResponse) => this.handleError(error, 'Error removing from watchlist')
    });
  }

  userAuthenticate(): boolean {
    return !!this.authService.getToken();
  }

  redirect() {
    this.closeModalListener();
    this.router.navigate(["/SignIn"]);
  }

  handleError(error: HttpErrorResponse, message: string) {
    if (error.status === 401 || error.status === 403) {
      window.dispatchEvent(new CustomEvent('closeModalEvent'));
      this.router.navigate(['/SignIn']);
    }
  }
}


