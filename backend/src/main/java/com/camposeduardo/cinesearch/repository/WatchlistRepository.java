package com.camposeduardo.cinesearch.repository;

import com.camposeduardo.cinesearch.entities.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {

}
