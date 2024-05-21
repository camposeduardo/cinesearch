import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { Movie } from '../model/Movie';
import { environment } from 'src/environments/environment.development';
import { MovieInfo } from '../model/MovieInfo';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  private moviesRelatedSubject = new BehaviorSubject<Movie[] | null>(null);
  movies = this.moviesRelatedSubject.asObservable();

  constructor(private http: HttpClient){}

  searchRelatedMovies(title: string) {
    return this.http.get<Movie[]>(`${environment.apiUrl}/search/${title}`)
    .pipe(map(response => {
      this.moviesRelatedSubject?.next(response)
    }), (err) => {
      return err;
    });
  }

  searchMovieByImdbId(imdbId: string) {
    return this.http.get<MovieInfo>(`${environment.apiUrl}/search/imdb/${imdbId}`);
  }
}
