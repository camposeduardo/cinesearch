import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MovieInfo } from '../model/MovieInfo';
import { BehaviorSubject, map } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Movie } from '../model/Movie';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {

  private watchlistMoviesSubject = new BehaviorSubject<MovieInfo[] | null>(null);
  watchlistMovies = this.watchlistMoviesSubject.asObservable();

  constructor(private http: HttpClient){}

  addToWatchlist(movie: MovieInfo, email: string) {
    // Mudar para interceptors
    const token = JSON.parse(localStorage.getItem("token")!).token;
    return this.http.post<MovieInfo[]>(`${environment.apiUrl}/watchlist/add`, movie,{headers: {'Authorization': `Bearer ${token}`}, params: {
      email: email
    }})
    .pipe(map(response => {

    }));
  }

  getAllMoviesInWatchlist( email: string) {
    // Mudar para interceptors
    const token = JSON.parse(localStorage.getItem("token")!).token;
    return this.http.get<MovieInfo[]>(`${environment.apiUrl}/watchlist`,{headers: {'Authorization': `Bearer ${token}`}, params: {
      email: email
    }})
    .pipe(map(response => {
      this.watchlistMoviesSubject.next(response)
    }));
  }
}
