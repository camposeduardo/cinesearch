package com.camposeduardo.cinesearch.service;

import com.camposeduardo.cinesearch.entities.MovieInfo;
import com.camposeduardo.cinesearch.entities.Watchlist;
import com.camposeduardo.cinesearch.entities.WatchlistMovie;
import com.camposeduardo.cinesearch.exceptions.WatchlistNotFoundException;
import com.camposeduardo.cinesearch.repository.MovieRepository;
import com.camposeduardo.cinesearch.repository.UserRepository;
import com.camposeduardo.cinesearch.repository.WatchlistMovieRepository;
import com.camposeduardo.cinesearch.repository.WatchlistRepository;
import com.camposeduardo.cinesearch.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WatchlistServiceTest {

    @InjectMocks
    private WatchlistService watchlistService;

    @Mock
    private MovieService movieService;

    @Mock
    private WatchlistMovieRepository watchlistMovieRepository;

    @Mock
    private WatchlistRepository watchlistRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    private MovieInfo movie;
    private Watchlist watchlist;

    private User user;

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

        user = new User();

        watchlist = new Watchlist();
        watchlist.setId(1);
        watchlist.setUser(user);
        watchlist.setMovies(null);

    }

    @Test
    public void testGetWatchlistByEmailWhenWatchlistExists() {
        String email = "test@example.com";

        when(userRepository.findWatchlistIdByEmail(email)).thenReturn(Optional.of(watchlist.getId()));
        when(watchlistRepository.findById(watchlist.getId())).thenReturn(Optional.of(watchlist));

        Watchlist result = watchlistService.getWatchlist(email);

        assertNotNull(result);
        assertEquals(watchlist, result);
    }

    @Test
    public void shouldThrowAExceptionWhenGetWatchlistByEmailIsBlank() {
        String email = " ";

        assertThrows(WatchlistNotFoundException.class, () -> {
            watchlistService.getWatchlist(email);
        });
    }

    @Test
    public void shouldThrowExceptionWhenWatchlistDoesntExists() {
        String email = "test@email";

        when(userRepository.findWatchlistIdByEmail(email)).thenReturn(Optional.empty());

        assertThrows(WatchlistNotFoundException.class, () -> {
            watchlistService.getWatchlist(email);
        });
    }

    @Test
    public void shouldReturnTrueIfMovieIsInWatchlist() {
        List<Integer> ids = Arrays.asList(1,2,3,4,5);
        when(watchlistMovieRepository.
                findMovieIdByWatchlistId(watchlist.getId())).thenReturn(Optional.of(ids));
        when(movieService.getMovieByImdbId(movie.getImdbId())).thenReturn(movie);

        assertTrue(watchlistService.checkIfMovieExistsInWatchlist(watchlist.getId(), movie));
    }

    @Test
    public void shouldReturnFalseIfMovieIsNotInWatchlist() {
        List<Integer> ids = Arrays.asList(2,3,4,5);
        when(watchlistMovieRepository.
                findMovieIdByWatchlistId(watchlist.getId())).thenReturn(Optional.of(ids));
        when(movieService.getMovieByImdbId(movie.getImdbId())).thenReturn(movie);

        assertFalse(watchlistService.checkIfMovieExistsInWatchlist(1, movie));
    }


    @Test
    public void shouldReturnAMovieWhenAddMovieToWatchlistAndMovieExistsInDBAndNotInWatchlist() {
        String email = "test@example.com";
        Optional<Integer> watchlistId = Optional.of(1);

        when(userRepository.findWatchlistIdByEmail(email)).thenReturn(watchlistId);
        when(watchlistRepository.findById(watchlistId.get())).thenReturn(Optional.of(watchlist));

        List<Integer> movieIds = new ArrayList<>();
        Optional<List<Integer>> allUserMovies = Optional.of(movieIds);

        when(watchlistMovieRepository.findMovieIdByWatchlistId(watchlist.getId())).thenReturn(allUserMovies);
        when(movieService.getMovieByImdbId(movie.getImdbId())).thenReturn(movie);

        MovieInfo result = watchlistService.addMovieToWatchlist(email, movie);

        assertEquals(movie, result);
        verify(movieService, times(0)).addMovie(movie);
        verify(watchlistMovieRepository, times(1)).save(any(WatchlistMovie.class));
    }

    @Test
    public void shouldReturnAMovieWhenAddMovieToWatchlistAndMovieDoesNotExistInDBAndNotInWatchlist() throws Exception {
        String email = "test@example.com";
        Optional<Integer> watchlistId = Optional.of(1);

        when(userRepository.findWatchlistIdByEmail(email)).thenReturn(watchlistId);
        when(watchlistRepository.findById(watchlistId.get())).thenReturn(Optional.of(watchlist));
        when(movieService.getMovieByImdbId(movie.getImdbId())).thenReturn(null);
        when(movieService.addMovie(movie)).thenReturn(movie);

        MovieInfo result = watchlistService.addMovieToWatchlist(email, movie);

        assertEquals(movie, result);
        verify(movieService, times(1)).addMovie(movie);
        verify(watchlistMovieRepository, times(1)).save(any(WatchlistMovie.class));
    }

    @Test
    public void itShouldReturnAllTheMoviesInTheUserWatchlist() {

        Optional<Integer> watchlistId = Optional.of(1);

        when(userRepository.findWatchlistIdByEmail("test@email.com")).thenReturn(watchlistId);
        when(watchlistRepository.findById(watchlistId.get())).thenReturn(Optional.of(watchlist));

        List<MovieInfo> allMovies = Arrays.asList(movie, new MovieInfo());

        when(watchlistMovieRepository
                .findMoviesByWatchlistId(watchlist.getId())).thenReturn(Optional.of(allMovies));

        List<MovieInfo> allUserMovies = watchlistService.getAllMoviesInWatchlist("test@email.com");
        assertEquals(2, allUserMovies.size());
        assertEquals("The Shawshank Redemption", allUserMovies.get(0).getTitle());
        assertNull(allUserMovies.get(1).getId());
    }

}
