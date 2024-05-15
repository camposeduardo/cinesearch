package com.camposeduardo.cinesearch.controller;

import com.camposeduardo.cinesearch.entities.Movie;
import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchMovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{title}")
    public ResponseEntity<List<Movie>> searchMovie(@PathVariable String title) {
        return ResponseEntity.ok(movieService.search(title));
    }

    @GetMapping("/imdb/{imdbId}")
    public ResponseEntity<MovieInfo> searchMovieByImdbId(@PathVariable String imdbId) {
        System.out.println("by imdbid");
        return ResponseEntity.ok(movieService.searchByImdbId(imdbId));
    }
}
