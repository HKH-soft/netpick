package com.hossein.spring_project.journey;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.hossein.spring_project.auth.AuthenticationRequest;
import com.hossein.spring_project.customer.CustomerDTO;
import com.hossein.spring_project.customer.CustomerRegistrationRequest;
import com.hossein.spring_project.jwt.JWTUtil;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthenticationIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    private static final Random RANDOM = new Random();
    private static final String AUTHENTICATION_PATH = "/v1/auth";
    private static final String CUSTOMER_PATH = "/v1/customers";

    @Test
    void canLogin(){
        // create reistration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName(),
                email = fakerName.lastName() + UUID.randomUUID() + "@example.com";
        int age = RANDOM.nextInt(10,90);
        String password = "password";

        boolean gender = true;
        CustomerRegistrationRequest registerationRequest = new CustomerRegistrationRequest(
        name, email, password, age,gender
        );

        AuthenticationRequest authRequest = new AuthenticationRequest(
            email,password
        );

        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequest),AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();
                    
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registerationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        EntityExchangeResult<CustomerDTO> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequest),AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult();
        
        String jwtToken = result.getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);
        CustomerDTO customerDTO = result.getResponseBody();


        assertThat(jwtUtil.isTokenValid(jwtToken,customerDTO.username())).isTrue();

        assertThat(customerDTO.email()).isEqualTo(email);
        assertThat(customerDTO.age()).isEqualTo(age);
        assertThat(customerDTO.gender()).isEqualTo(gender);
        assertThat(customerDTO.username()).isEqualTo(email);
        assertThat(customerDTO.name()).isEqualTo(name);
        assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));
    }
}
