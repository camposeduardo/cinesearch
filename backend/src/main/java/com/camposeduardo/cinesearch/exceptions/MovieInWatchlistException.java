package com.camposeduardo.cinesearch.exceptions;

public class MovieInWatchlistException extends RuntimeException {

    public MovieInWatchlistException () { super("Movie already in the watchlist"); }

    public MovieInWatchlistException (String message) { super(message); }
}
