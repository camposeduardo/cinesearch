package com.camposeduardo.cinesearch.controller;

import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.exceptions.MovieInWatchlistException;
import com.camposeduardo.cinesearch.service.JwtService;
import com.camposeduardo.cinesearch.service.WatchlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(WatchlistController.class)
public class WatchlistControllerTest {

    @MockBean
    WatchlistService watchlistService;

    @MockBean
    JwtService jwtService;

    @MockBean
    AuthenticationProvider authenticationProvider;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    MovieInfo movie;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        movie= new MovieInfo();
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
    @WithMockUser(username = "test@email.com", roles = "USER")
    public void itShouldAddMovieToWatchlistAndReturnMovie() throws Exception {
        when(watchlistService.addMovieToWatchlist("test@email.com", movie)).thenReturn(movie);

        mockMvc.perform(post("/watchlist/add")
                        .param("email", "test@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Shawshank Redemption"))
                .andExpect(jsonPath("$.director").value("Frank Darabont"));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = "USER")
    public void itShouldThrowAnExceptionWhenMovieIsAlreadyInWatchlist() throws Exception {
        when(watchlistService.addMovieToWatchlist("test@email.com", movie))
                .thenThrow(new MovieInWatchlistException());

        mockMvc.perform(post("/watchlist/add")
                        .content(objectMapper.writeValueAsString(movie))
                        .param("email", "test@email.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Movie already in the watchlist"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = "USER")
    public void itShouldReturnAllTheMoviesInWatchlist() throws Exception {

        MovieInfo anotherMovie = new MovieInfo();
        anotherMovie.setId(2);
        anotherMovie.setTitle("The Godfather");
        anotherMovie.setYear("1972");
        anotherMovie.setRated("R");
        anotherMovie.setReleased("24 Mar 1972");
        anotherMovie.setRuntime("175 min");
        anotherMovie.setGenre("Crime, Drama");
        anotherMovie.setDirector("Francis Ford Coppola");
        anotherMovie.setActors("Marlon Brando, Al Pacino, James Caan");
        anotherMovie.setPlot("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.");
        anotherMovie.setPoster("https://example.com/godfather_poster.jpg");
        anotherMovie.setImdbRating("9.2");
        anotherMovie.setImdbId("tt0068646");

        List<MovieInfo> allUserMovies = Arrays.asList(movie, anotherMovie);

        when(watchlistService.getAllMoviesInWatchlist("test@email.com")).thenReturn(allUserMovies);

        mockMvc.perform(get("/watchlist")
                        .param("email", "test@email.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value(movie.getTitle()))
                .andExpect(jsonPath("$[1].title").value(anotherMovie.getTitle()));

    }

    @Test
    @WithMockUser(username = "test@email.com", roles = "USER")
    public void itShouldReturnNullWhenWatchlistDoesntHaveMovies() throws Exception {
        when(watchlistService.getAllMoviesInWatchlist("test@email.com")).thenReturn(null);

        mockMvc.perform(get("/watchlist")
                        .param("email", "test@email.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
