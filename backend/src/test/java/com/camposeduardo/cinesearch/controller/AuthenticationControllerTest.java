package com.camposeduardo.cinesearch.controller;

import com.camposeduardo.cinesearch.entities.AuthenticationResponse;
import com.camposeduardo.cinesearch.entities.RegisterRequest;
import com.camposeduardo.cinesearch.service.AuthenticationService;
import com.camposeduardo.cinesearch.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
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

    @Test
    public void it_should_return_true_if_email_is_already_taken() throws Exception {

        String email = "test@example.com";
        boolean emailExists = true;

        // Configura o comportamento do mock
        when(authenticationService.checkEmail(email)).thenReturn(emailExists);

        // Realiza a requisição GET e verifica o resultado
        mockMvc.perform(MockMvcRequestBuilders
                .get("/" + email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void it_should_return_false_if_email_is_not_taken() throws Exception {

        String email = "test@example.com";
        boolean emailExists = false;

        // Configura o comportamento do mock
        when(authenticationService.checkEmail(email)).thenReturn(emailExists);

        // Realiza a requisição GET e verifica o resultado
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void it_should_return_null_when_doesnt_have_all_the_fields() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("john@email.com");
        request.setPassword("1234");


        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("null"));
    }

    // Throws exception when doenst have all the fields
    // Throws exception when credentials it`s not valid
}
