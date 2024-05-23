package com.camposeduardo.cinesearch.exceptions;

public class MovieInWatchlistNotFoundException extends RuntimeException {

    public MovieInWatchlistNotFoundException () { super("Movie not found in the watchlist"); }

    public MovieInWatchlistNotFoundException (String message) { super(message); }
}
