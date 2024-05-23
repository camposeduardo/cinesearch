package com.camposeduardo.cinesearch.exceptions;

public class WatchlistNotFoundException extends RuntimeException {

    public WatchlistNotFoundException () { super("Watchlist not found"); }

    public WatchlistNotFoundException (String message) { super(message); }
}
