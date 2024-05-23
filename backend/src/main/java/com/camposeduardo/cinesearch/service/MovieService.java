package com.camposeduardo.cinesearch.service;

import com.camposeduardo.cinesearch.entities.Movie;
import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.entities.SearchMovieResponse;
import com.camposeduardo.cinesearch.exceptions.MovieNotFoundException;
import com.camposeduardo.cinesearch.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Value("${omdbapi.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    private final String OMDB_API_KEY = System.getenv("OMDB_API_KEY");

    public List<Movie> search(String title) {

        String fullUrl = String.format(url,OMDB_API_KEY, "s", title);

        ResponseEntity<SearchMovieResponse> response = restTemplate.getForEntity(fullUrl,
                SearchMovieResponse.class);

        if (response.getBody() == null)  {
            throw new MovieNotFoundException();
        }

        return response.getBody().movies();
    }

    public MovieInfo searchByImdbId(String imdbId) {
        String fullUrl = String.format(url, OMDB_API_KEY, "i", imdbId);

        ResponseEntity<MovieInfo> response = restTemplate.getForEntity(fullUrl , MovieInfo.class);

        if (response.getBody() == null)  {
            throw new MovieNotFoundException();
        }

        return response.getBody();
    }

    public MovieInfo addMovie(MovieInfo movie) {
        Optional<MovieInfo> movieToSearchInDB = movieRepository.findByImdbId(movie.getImdbId());
        return movieToSearchInDB.orElseGet(() -> movieRepository.save(movie));
    }

    public MovieInfo getMovieByImdbId(String imdbId) {
        Optional<MovieInfo> movie =  movieRepository.findByImdbId(imdbId);

        return movie.orElse(null);
    }
}
