package com.camposeduardo.cinesearch.service;

import com.camposeduardo.cinesearch.entities.Movie;
import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.entities.Watchlist;
import com.camposeduardo.cinesearch.entities.WatchlistMovie;
import com.camposeduardo.cinesearch.repository.UserRepository;
import com.camposeduardo.cinesearch.repository.WatchlistMovieRepository;
import com.camposeduardo.cinesearch.repository.WatchlistRepository;
import com.camposeduardo.cinesearch.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

@Service
public class WatchlistService {
    @Autowired
    private WatchlistMovieRepository watchlistMovieRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieService movieService;

    public MovieInfo addMovieToWatchlist(String email, MovieInfo movie) throws Exception {

        Optional<Integer> watchListId = userRepository.findWatchlistIdByEmail(email);

        if (watchListId.isEmpty()) {
            return null;
        }

        Optional<Watchlist> watchlist = watchlistRepository.findById(watchListId.get());

        MovieInfo movieInDB = movieService.getMovieByImdbId(movie.getImdbId());

        if (movieInDB == null) {
            movie = movieService.addMovie(movie);
        } else {
            Optional<List<Integer>> allUserMovies = watchlistMovieRepository.
                    findMovieIdByWatchlistId(watchListId.get());

            if (!allUserMovies.get().contains(movieInDB.getId())) {
                movie = movieInDB;
            }
            else {
                throw new InputMismatchException("Movie already in watchlist");
            }
        }

        WatchlistMovie watchlistMovie = new WatchlistMovie();
        watchlistMovie.setMovie(movie);
        watchlistMovie.setWatchlist(watchlist.get());
        watchlistMovieRepository.save(watchlistMovie);
        return movie;
    }

    public List<MovieInfo>getAllMoviesInWatchlist(String email) {

        Optional<Integer> watchlistId = userRepository.findWatchlistIdByEmail(email);

        if (watchlistId.isEmpty()) {
            return null;
        }

        Optional<List<MovieInfo>> allMoviesInWatchlist = watchlistMovieRepository
                .findMovieByWatchlistId(watchlistId.get());

        return allMoviesInWatchlist.get();
    }
}
