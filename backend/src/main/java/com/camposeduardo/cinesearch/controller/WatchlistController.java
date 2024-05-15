package com.camposeduardo.cinesearch.controller;

import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @PostMapping("/add")
    public ResponseEntity<MovieInfo> addToWatchlist(@RequestBody MovieInfo movie,
                                                    @RequestParam(value="email") String email) throws Exception {
        return ResponseEntity.ok(watchlistService.addMovieToWatchlist(email,movie));
    }

    @GetMapping
    public ResponseEntity<List<MovieInfo>> getWatchlistMovies(@RequestParam(value="email") String email) {
        return ResponseEntity.ok(watchlistService.getAllMoviesInWatchlist(email));
    }
}
