package com.camposeduardo.cinesearch.repository;

import com.camposeduardo.cinesearch.entities.Watchlist;
import com.camposeduardo.cinesearch.entities.WatchlistMovie;
import com.camposeduardo.cinesearch.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u.watchlist.id FROM User u WHERE u.email = :email")
    Optional<Integer> findWatchlistIdByEmail(String email);

    Optional<User> findByEmail(String email);
}
