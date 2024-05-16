import { Component } from '@angular/core';
import { WatchlistService } from '../service/watchlist.service';
import { Movie } from '../model/Movie';
import { MovieInfo } from '../model/MovieInfo';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent {

  watchlistMovies: MovieInfo[] | null= [];
  movieInfo: MovieInfo | undefined;

  constructor(private watchlistService: WatchlistService) {}

  ngOnInit() {
    this.getAllMoviesInWatchlist();
  }

  getAllMoviesInWatchlist() {
    this.watchlistService.watchlistMovies.subscribe(data => {
      this.watchlistMovies = data;
    })
  }

  getMovieInformation(imdbId: string) {
    this.movieInfo = this.watchlistMovies?.find((movie) => movie.imdbId === imdbId);
  }

}
