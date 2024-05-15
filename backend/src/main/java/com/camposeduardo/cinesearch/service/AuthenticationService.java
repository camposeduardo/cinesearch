package com.camposeduardo.cinesearch.service;

import com.camposeduardo.cinesearch.entities.AuthenticationRequest;
import com.camposeduardo.cinesearch.entities.AuthenticationResponse;
import com.camposeduardo.cinesearch.entities.RegisterRequest;
import com.camposeduardo.cinesearch.entities.Watchlist;
import com.camposeduardo.cinesearch.repository.UserRepository;
import com.camposeduardo.cinesearch.repository.WatchlistRepository;
import com.camposeduardo.cinesearch.user.Role;
import com.camposeduardo.cinesearch.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final WatchlistRepository watchlistRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        Watchlist watchlist = new Watchlist();
        user.setWatchlist(watchlist);
        watchlistRepository.save(watchlist);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
