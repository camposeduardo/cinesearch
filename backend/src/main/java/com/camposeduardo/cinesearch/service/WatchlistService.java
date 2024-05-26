package com.camposeduardo.cinesearch.service;

import com.camposeduardo.cinesearch.entities.Movie;
import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.entities.Watchlist;
import com.camposeduardo.cinesearch.entities.WatchlistMovie;
import com.camposeduardo.cinesearch.exceptions.MovieInWatchlistException;
import com.camposeduardo.cinesearch.exceptions.MovieInWatchlistNotFoundException;
import com.camposeduardo.cinesearch.exceptions.WatchlistNotFoundException;
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
        Watchlist watchlist = getWatchlist(email);
        MovieInfo movieInDB = movieService.getMovieByImdbId(movie.getImdbId());

        if (movieInDB == null) {
            movie = movieService.addMovie(movie);
        } else {
            if (checkIfMovieExistsInWatchlist(watchlist.getId(), movieInDB)) {
                throw new MovieInWatchlistException();
            }
            else {
                movie = movieInDB;
            }
        }
        WatchlistMovie watchlistMovie = new WatchlistMovie();
        watchlistMovie.setMovie(movie);
        watchlistMovie.setWatchlist(watchlist);
        watchlistMovieRepository.save(watchlistMovie);
        return movie;
    }

    public List<MovieInfo>getAllMoviesInWatchlist(String email) {

        Watchlist watchlist = getWatchlist(email);

        if (watchlist == null) {
            throw new WatchlistNotFoundException();
        }

        Optional<List<MovieInfo>> allMoviesInWatchlist = watchlistMovieRepository
                .findMoviesByWatchlistId(watchlist.getId());

        return allMoviesInWatchlist.get();
    }

    public Watchlist getWatchlist(String email) {
        Optional<Integer> watchListId = userRepository.findWatchlistIdByEmail(email);
        if (watchListId.isEmpty()) {
            throw new WatchlistNotFoundException();
        }

        return watchlistRepository.findById(watchListId.get()).orElse(null);
    }

    public boolean checkIfMovieExistsInWatchlist(Integer watchlistId, MovieInfo movie) {
        Optional<List<Integer>> allUserMovies = watchlistMovieRepository.
                findMovieIdByWatchlistId(watchlistId);
        MovieInfo movieInDB = movieService.getMovieByImdbId(movie.getImdbId());
        if (movieInDB == null) {
            movieInDB = movieService.addMovie(movie);
        }
        return allUserMovies.get().contains(movieInDB.getId());
    }

    public boolean checkIfMovieExistsInWatchlist(String email, MovieInfo movie) {
        Optional<Integer> watchlistId = userRepository.findWatchlistIdByEmail(email);
        if (watchlistId.isEmpty()) {
            throw new WatchlistNotFoundException();
        }
        return checkIfMovieExistsInWatchlist(watchlistId.get(), movie);
    }

    public void removeMovieFromWatchlist(String email, MovieInfo movieInfo) {
        Watchlist watchlist = getWatchlist(email);

        if (watchlist == null) {
            throw new WatchlistNotFoundException();
        }

        if (!checkIfMovieExistsInWatchlist(watchlist.getId(), movieInfo)) {
            throw new MovieInWatchlistNotFoundException();
        }

        MovieInfo movie = movieService.getMovieByImdbId(movieInfo.getImdbId());
        Optional<WatchlistMovie> wm = watchlistMovieRepository.findByWatchlistIdAndMovieId(watchlist.getId(),
                movie.getId());

        if (wm.isEmpty()) {
            throw new MovieInWatchlistNotFoundException();
        }

        watchlistMovieRepository.delete(wm.get());
    }
}
