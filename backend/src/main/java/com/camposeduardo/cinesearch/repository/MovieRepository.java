package com.camposeduardo.cinesearch.repository;

import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.entities.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieInfo, Integer> {

    Optional<MovieInfo> findByImdbId(String imdbId);
}
