package com.camposeduardo.cinesearch.controller;

import com.camposeduardo.cinesearch.service.AuthenticationService;
import com.camposeduardo.cinesearch.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    JwtService jwtService;

    @MockBean
    AuthenticationProvider authenticationProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void itShouldReturnTrueIfEmailIsAlreadyTaken() throws Exception {

        String email = "test@example.com";
        boolean emailExists = true;

        when(authenticationService.checkEmail(email)).thenReturn(emailExists);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/" + email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void itShouldReturnFalseIfEmailIsNotTaken() throws Exception {

        String email = "test@example.com";
        boolean emailExists = false;

        when(authenticationService.checkEmail(email)).thenReturn(emailExists);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
