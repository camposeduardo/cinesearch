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

  constructor(private watchlistService: WatchlistService, private authService: AuthenticationService, private router: Router) { }

  ngOnInit() {
    this.getAllMoviesInWatchlist();
  }

  getAllMoviesInWatchlist() {
    this.watchlistService.getAllMoviesInWatchlist(this.authService.getEmail()).subscribe(
      {
        next: (data) => {
          this.watchlistMovies = data;
        },
        error: (error: HttpErrorResponse) => {
          // Some logic here
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
        this.closeModal!.nativeElement.click();
        this.getAllMoviesInWatchlist();
      },
      error: (error: HttpErrorResponse) => {
        // Some logic here
      }
    });
  }



}
