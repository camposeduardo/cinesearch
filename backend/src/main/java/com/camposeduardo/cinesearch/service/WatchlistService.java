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
        Watchlist watchlist = getWatchlist(email);
        MovieInfo movieInDB = movieService.getMovieByImdbId(movie.getImdbId());

        if (movieInDB == null) {
            movie = movieService.addMovie(movie);
        } else {
            boolean movieInWatchlist = checkIfMovieExistsInWatchlist(watchlist.getId(), movieInDB);
            if (movieInWatchlist) {
                throw new InputMismatchException("Movie already in watchlist");
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
            return null;
        }

        Optional<List<MovieInfo>> allMoviesInWatchlist = watchlistMovieRepository
                .findMovieByWatchlistId(watchlist.getId());

        return allMoviesInWatchlist.get();
    }

    public Watchlist getWatchlist(String email) {
        Optional<Integer> watchListId = userRepository.findWatchlistIdByEmail(email);

        if (watchListId.isEmpty()) {
            return null;
        }

        return watchlistRepository.findById(watchListId.get()).orElse(null);
    }

    public boolean checkIfMovieExistsInWatchlist(Integer watchlistId, MovieInfo movie) {
        Optional<List<Integer>> allUserMovies = watchlistMovieRepository.
                findMovieIdByWatchlistId(watchlistId);
        return allUserMovies.get().contains(movie.getId());
    }
}
