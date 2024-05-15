package com.camposeduardo.cinesearch.repository;

import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.entities.WatchlistMovie;
import com.camposeduardo.cinesearch.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WatchlistMovieRepository extends JpaRepository<WatchlistMovie, Integer> {

    Optional<WatchlistMovie> findByWatchlistId(Integer watchlistId);

    @Query("SELECT wm.movie.id FROM WatchlistMovie wm WHERE wm.watchlist.id = :watchlistId")
    Optional<List<Integer>> findMovieIdByWatchlistId(Integer watchlistId);


    @Query("SELECT mi FROM MovieInfo mi WHERE mi.id IN " +
            "(SELECT wm.movie.id FROM WatchlistMovie wm WHERE wm.watchlist.id = :watchlistId)")
    Optional<List<MovieInfo>> findMovieByWatchlistId(Integer watchlistId);

}
