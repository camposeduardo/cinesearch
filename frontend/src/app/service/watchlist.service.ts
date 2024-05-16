import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MovieInfo } from '../model/MovieInfo';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {

  constructor(private http: HttpClient, private authService: AuthenticationService){}

  addToWatchlist(movie: MovieInfo, email: string) {
    return this.http.post<MovieInfo[]>(`${environment.apiUrl}/watchlist/add`, movie, {params: {
      email: email
    }});
  }

  getAllMoviesInWatchlist(email: string): Observable<MovieInfo[]> {
    return this.http.get<MovieInfo[]>(`${environment.apiUrl}/watchlist`, {params: {
      email: email}});
  }

}
