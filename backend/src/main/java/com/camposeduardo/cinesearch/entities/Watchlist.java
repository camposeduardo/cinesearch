package com.camposeduardo.cinesearch.entities;

import com.camposeduardo.cinesearch.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "watchlist")
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "watchlist", cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "watchlist")
    private List<WatchlistMovie> movies;
}
