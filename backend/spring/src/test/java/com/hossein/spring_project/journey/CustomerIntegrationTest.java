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
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.hossein.spring_project.customer.CustomerDTO;
import com.hossein.spring_project.customer.CustomerRegistrationRequest;
import com.hossein.spring_project.customer.CustomerUpdateRequest;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

@Autowired
private WebTestClient webTestClient;

private static final Random RANDOM = new Random();
private static final String CUSTOMER_PATH = "/v1/customers";
@Test
void canRegisterACustomer(){
        // create reistration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName(),
                email = fakerName.lastName() + UUID.randomUUID() + "@example.com";
        int age = RANDOM.nextInt(10,90);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
        name, email, "password", age,true
        );

        // send a post request

        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();
        

        // get customer by id
        int id = allCustomers.stream()
        .filter(e -> e.email().equals(email))
        .map(CustomerDTO::id)
        .findFirst()
        .orElseThrow();

        // make sure that customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                true,
                age,
                List.of("ROLE_USER"),
                email
        );
        
        
        assertThat(allCustomers)
                .contains(expectedCustomer);
        

        webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
                .isEqualTo(expectedCustomer);
}

@Test
void canDeleteCustomer(){
        // create reistration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName(),
                email = fakerName.lastName() + UUID.randomUUID() + "@example.com";
        int age = RANDOM.nextInt(10,90);

        CustomerRegistrationRequest tokenRequest = new CustomerRegistrationRequest(
        name, email + UUID.randomUUID(), "password", age,true
        );
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
        name, email, "password", age,true
        );
        // send a post request
        
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(tokenRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);
        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();
        

        // get customer by id
        int id = allCustomers.stream()
                .filter(e -> e.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // delete cusotmer

        webTestClient.delete()
                .uri(CUSTOMER_PATH + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk();
        
        webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isNotFound();
}

@Test
void canUpdateCustomer(){
        // create reistration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName(),
                email = fakerName.lastName() + UUID.randomUUID() + "@example.com";
        int age = RANDOM.nextInt(10,90);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
        name, email, "password", age,true
        );
        // send a post request
        
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();
        

        // get customer by id
        int id = allCustomers.stream()
                .filter(e -> e.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // update customer

        CustomerDTO update = new CustomerDTO(
                id,
                fakerName.name(),
                email,
                true,
                age,
                List.of("ROLE_USER"),
                email
        );

        webTestClient.put()
                .uri(CUSTOMER_PATH + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(update), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        
        CustomerDTO returnedCustomer = webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        assertThat(returnedCustomer).isEqualTo(update);
}
}
