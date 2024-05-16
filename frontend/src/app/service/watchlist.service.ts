import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MovieInfo } from '../model/MovieInfo';
import { BehaviorSubject, map } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Movie } from '../model/Movie';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {

  private watchlistMoviesSubject = new BehaviorSubject<MovieInfo[] | null>(null);
  watchlistMovies = this.watchlistMoviesSubject.asObservable();

  constructor(private http: HttpClient, private authService: AuthenticationService){}

  addToWatchlist(movie: MovieInfo, email: string) {
    return this.http.post<MovieInfo[]>(`${environment.apiUrl}/watchlist/add`, movie, {params: {
      email: email
    }});
  }

  getAllMoviesInWatchlist(email: string) {
    return this.http.get<MovieInfo[]>(`${environment.apiUrl}/watchlist`, {params: {
      email: email
    }})
    .pipe(map(response => {
      this.watchlistMoviesSubject.next(response)
    }));
  }
}
