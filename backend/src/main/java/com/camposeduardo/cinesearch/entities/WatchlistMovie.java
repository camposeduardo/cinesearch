package com.camposeduardo.cinesearch.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "watchlist_movie")
public class WatchlistMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name= "watchlist_id")
    private Watchlist watchlist;

    @ManyToOne
    @JoinColumn(name= "movie_id")
    private MovieInfo movie;
}
