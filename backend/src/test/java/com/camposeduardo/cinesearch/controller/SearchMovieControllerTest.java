package com.camposeduardo.cinesearch.controller;

import com.camposeduardo.cinesearch.entities.Movie;
import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.entities.SearchMovieResponse;
import com.camposeduardo.cinesearch.exceptions.MovieNotFoundException;
import com.camposeduardo.cinesearch.service.AuthenticationService;
import com.camposeduardo.cinesearch.service.JwtService;
import com.camposeduardo.cinesearch.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchMovieController.class)
public class SearchMovieControllerTest {

    @MockBean
    MovieService movieService;

    @MockBean
    JwtService jwtService;

    @MockBean
    AuthenticationProvider authenticationProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void it_should_return_a_list_of_movies() throws Exception {

        String title = "Movie";

        Movie movie1 = new Movie("Movie 1", "2010", "N/A", "tt1234");
        Movie movie2 = new Movie("Movie 2", "2010", "N/A", "tt3412");

        List<Movie> movies = Arrays.asList(movie1, movie2);

        when(movieService.search(title)).thenReturn(movies);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/search/" + title)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(movie1.getTitle()))
                .andExpect(jsonPath("$[1].title").value(movie2.getTitle()));
    }

    @Test
    public void it_should_throw_an_exception_when_title_is_blank() throws Exception {

        String title = " ";

        when(movieService.search(title)).thenThrow(new MovieNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/search/" + title)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }

    @Test
    public void it_should_throw_an_exception_when_movies_are_not_founded() throws Exception {

        String title = "NonExistentMovie";

        when(movieService.search(title)).thenThrow(new MovieNotFoundException("Movie not found"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/search/" + title)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }

    @Test
    public void it_should_return_a_movie_by_imdb_id() throws Exception {

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

        String imdbId = "tt0111161";


        when(movieService.searchByImdbId(imdbId)).thenReturn(movie);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/search//imdb/" +  imdbId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movie.getTitle()));

    }


}

