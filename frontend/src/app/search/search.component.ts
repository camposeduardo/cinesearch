import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MovieService } from '../service/movie.service';
import { Movie } from '../model/Movie';
import { ActivatedRoute } from '@angular/router';
import { MovieInfo } from '../model/MovieInfo';
import { WatchlistService } from '../service/watchlist.service';
import { AuthenticationService } from '../service/authentication.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {

  titleToSearch!: string;

  moviesFounded: Movie[] | null = [];

  movieInfo: MovieInfo | undefined;

  constructor(private movieService: MovieService, private watchlistService: WatchlistService,
    private authService: AuthenticationService, private route: ActivatedRoute) {

  }

  ngOnInit() {
    this.getMovies();
    this.titleToSearch = "";
  }

  ngDoCheck() {
    this.titleToSearch = this.route.snapshot.params['title'];
  }

  getMovies() {
    this.movieService.movies.subscribe(data => {
      this.moviesFounded = data;
    })
  }

  getMovieInformation(imdbId: string) {
    this.movieService.searchMovieByImdbId(imdbId).subscribe(
      response => {
        if (response != null) {
          console.log("search");
          this.movieInfo = response;
          console.log(this.movieInfo);
        }

      }
    );
  }

  addToWatchlist(movie: MovieInfo) {
    const email = this.authService.getEmail();
    this.watchlistService.addToWatchlist(movie, email).subscribe(
      response => {
        console.log(response);
      }
    )
  }
}


