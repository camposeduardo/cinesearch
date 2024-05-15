package com.camposeduardo.cinesearch.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;

    private String email;

    private String password;
}
