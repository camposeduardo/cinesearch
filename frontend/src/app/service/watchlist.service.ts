import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MovieInfo } from '../model/MovieInfo';
import { BehaviorSubject, Observable, map} from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {

  private watchlistMoviesSubject = new BehaviorSubject<MovieInfo[] | null>(null);
  watchlist = this.watchlistMoviesSubject.asObservable();

  constructor(private http: HttpClient, private authService: AuthenticationService) { }

  addToWatchlist(movie: MovieInfo) {
    return this.http.post<MovieInfo[]>(`${environment.apiUrl}/watchlist/add`, movie, {
      params: {
        email: this.authService.getEmail()
      }
    });
  }

  getAllMoviesInWatchlist(): Observable<MovieInfo[]> {
    return this.http.get<MovieInfo[]>(`${environment.apiUrl}/watchlist`, { params: { email: this.authService.getEmail() } }).pipe(
      map(response => {
        this.watchlistMoviesSubject?.next(response)
        return response;
      }), (err) => {
        return err;
      }
    )
  }

  removeMovie(movieInfo: MovieInfo) {
    return this.http.delete<string>(`${environment.apiUrl}/watchlist/remove`, { headers: { email: this.authService.getEmail() }, body: movieInfo });
  }

  verifyIfMovieIsInWatchlist(movie: MovieInfo) {
    return this.http.post<boolean>(`${environment.apiUrl}/watchlist/movie/check`, movie, { params: { email: this.authService.getEmail() }});
  }

}

