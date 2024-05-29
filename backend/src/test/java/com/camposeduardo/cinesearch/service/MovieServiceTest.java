package com.camposeduardo.cinesearch.service;

import com.camposeduardo.cinesearch.entities.Movie;
import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.entities.SearchMovieResponse;
import com.camposeduardo.cinesearch.exceptions.MovieNotFoundException;
import com.camposeduardo.cinesearch.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    private MovieInfo movie;

    private final String OMDB_API_KEY = System.getenv("OMDB_API_KEY");

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        movie = new MovieInfo();
        movie.setId(1);
        movie.setTitle("The Shawshank Redemption");
        movie.setYear("1994");
        movie.setRated("R");
        movie.setReleased("14 Oct 1994");
        movie.setRuntime("142 min");
        movie.setGenre("Drama");
        movie.setDirector("Frank Darabont");
        movie.setActors("Tim Robbins, Morgan Freeman, Bob Gunton");
        movie.setPlot("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
        movie.setPoster("https://example.com/poster.jpg");
        movie.setImdbRating("9.3");
        movie.setImdbId("tt0111161");

    }


    @Test
    public void it_should_throw_an_exception_when_title_is_blank() {
        String title = " ";

        assertThrows(MovieNotFoundException.class, () -> {
            movieService.search(title);
        });
    }

    @Test
    public void it_should_throw_an_exception_when_response_body_is_null() {
        String title = "Inception";
        String fullUrl = "http://www.omdbapi.com/?apikey="+ OMDB_API_KEY + "&s=" + title+ "&type=movie";

        when(restTemplate.getForEntity(fullUrl, SearchMovieResponse.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class, () -> {
            movieService.search(title);
        });

        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    public void it_should_return_movies_when_title_is_valid() {
        String title = "Inception";
        String fullUrl = "http://www.omdbapi.com/?apikey="+ OMDB_API_KEY + "&s=" + title+ "&type=movie";

        Movie movie1 = new Movie("Inception", "2010", "N/A", "tt1375666");
        Movie movie2 = new Movie("Interstellar", "2014", "N/A", "tt0816692");
        List<Movie> movies = Arrays.asList(movie1, movie2);
        SearchMovieResponse searchMovieResponse = new SearchMovieResponse(movies);

        when(restTemplate.getForEntity(fullUrl, SearchMovieResponse.class))
                .thenReturn(new ResponseEntity<>(searchMovieResponse, HttpStatus.OK));

        List<Movie> result = movieService.search(title);
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        assertEquals("Interstellar", result.get(1).getTitle());
    }

    @Test
    public void it_should_return_a_single_movie_when_title_is_valid() {
        String imdbId = "tt0111161";
        String fullUrl = "http://www.omdbapi.com/?apikey="+ OMDB_API_KEY + "&i=" + imdbId + "&type=movie";

        MovieInfo movie = new MovieInfo(
                1,
                "The Shawshank Redemption",
                "1994",
                "R",
                "14 Oct 1994",
                "142 min",
                "Drama",
                "Frank Darabont",
                "Tim Robbins, Morgan Freeman, Bob Gunton",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                "https://example.com/poster.jpg",
                "9.3",
                "tt0111161"
        );

        when(restTemplate.getForEntity(fullUrl, MovieInfo.class))
                .thenReturn(new ResponseEntity<>(movie, HttpStatus.OK));

        assertEquals(movie, movieService.searchByImdbId(imdbId));
    }

    @Test
    public void it_should_throw_an_exception_when_imdb_id_is_blank() {

        String imdbId = " ";

        assertThrows(MovieNotFoundException.class, () -> {
            movieService.searchByImdbId(imdbId);
        });

    }

    @Test
    public void it_should_return_a_movie_when_save_in_db_and_movie_exists() {

        when(movieRepository.findByImdbId(movie.getImdbId())).thenReturn(Optional.of(movie));

        MovieInfo saveMovie = movieService.addMovie(movie);
        assertEquals(movie, saveMovie);
        verify(movieRepository, times(0)).save(movie);
    }

    @Test
    public void it_should_return_a_movie_when_save_in_db_and_movie_does_not_exist() {


        when(movieRepository.findByImdbId(movie.getImdbId())).thenReturn(Optional.empty());
        when(movieRepository.save(movie)).thenReturn(movie);

        MovieInfo saveMovie = movieService.addMovie(movie);

        assertEquals(movie, saveMovie);

        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    public void it_should_throw_an_exception_when_movie_is_null() {

        MovieInfo saveMovie = new MovieInfo();

        assertThrows(MovieNotFoundException.class, () -> {
            movieService.addMovie(saveMovie);
        });
    }


    @Test
    public void it_should_return_a_movie_when_getMovieByImdbId_and_movie_exists() {

        when(movieRepository.findByImdbId(movie.getImdbId())).thenReturn(Optional.of(movie));

        MovieInfo getMovie = movieService.getMovieByImdbId(movie.getImdbId());
        assertEquals(movie, getMovie);

        verify(movieRepository, times(1)).findByImdbId(movie.getImdbId());
    }

    @Test
    public void it_should_return_null_when_getMovieByImdbId_and_movie_does_not_exist() {

        when(movieRepository.findByImdbId(movie.getImdbId())).thenReturn(Optional.empty());

        MovieInfo getMovie = movieService.getMovieByImdbId(movie.getImdbId());
        assertNull(getMovie);

        verify(movieRepository, times(1)).findByImdbId(movie.getImdbId());
    }

}
