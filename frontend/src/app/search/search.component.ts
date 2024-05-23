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

  @ViewChild('closeModal') closeModal?: ElementRef;
  @ViewChild('alert') alert?: ElementRef;

  titleToSearch!: string;

  movieInWatchlist: boolean = false;

  alertMessage: string = '';
  alertType?: 'success' | 'danger';

  moviesFounded: Movie[] | null = [];

  movieInfo: MovieInfo | undefined;

  constructor(private movieService: MovieService, private watchlistService: WatchlistService,
    private authService: AuthenticationService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.getMovies();
    this.titleToSearch = "";
  }

  ngDoCheck() {
    this.titleToSearch = this.route.snapshot.params['title'];
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
          // Some logic here
        }
      }
    );
  }

  getMovieInformation(imdbId: string) {
    this.movieService.searchMovieByImdbId(imdbId).subscribe({
      next: (response) => {
        if (response != null) {
          this.movieInfo = response;
          this.verifyIfMovieIsInWatchlist(response);
        }
      },
      error: (error: HttpErrorResponse) => {
        // Some logic here
      }
    }
    );
  }

  verifyIfMovieIsInWatchlist(movie: MovieInfo) {
    this.watchlistService.verifyIfMovieIsInWatchlist(movie).subscribe(
      {
        next: (response) => {
          // simplify this
          if (response) {
            this.movieInWatchlist = true;
          }
          else {
            this.movieInWatchlist = false;
          }
        }
      }
    );
  }

  addToWatchlist(movie: MovieInfo) {
    this.watchlistService.addToWatchlist(movie)
      .subscribe({
        next: (response) => {
          this.closeModal!.nativeElement.click();
        },
        error: (error) => {
          this.alertMessage = `${error.error.message} `;
          this.alertType = "danger";
        }
      });
  }

  onRemoveMovieFromWatchlistButton(movie: MovieInfo) {
    this.watchlistService.removeMovie(movie).subscribe({
      next: (response) => {
        this.closeModal!.nativeElement.click();
      }
    });
  }


}


