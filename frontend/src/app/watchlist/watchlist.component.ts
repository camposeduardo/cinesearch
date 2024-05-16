import { Component, OnInit } from '@angular/core';
import { WatchlistService } from '../service/watchlist.service';
import { Movie } from '../model/Movie';
import { MovieInfo } from '../model/MovieInfo';
import { AuthenticationService } from '../service/authentication.service';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent {

  watchlistMovies: MovieInfo[]  = [];
  movieInfo: MovieInfo | undefined;

  constructor(private watchlistService: WatchlistService, private authService: AuthenticationService) {}

  ngOnInit () {
    this.getAllMoviesInWatchlist();
  }

  getAllMoviesInWatchlist() {
    this.watchlistService.getAllMoviesInWatchlist(this.authService.getEmail()).subscribe(
      data => {
        this.watchlistMovies = data;
      }
    )
  }

  getMovieInformation(imdbId: string) {
    this.movieInfo = this.watchlistMovies?.find((movie) => movie.imdbId === imdbId);
  }

}
