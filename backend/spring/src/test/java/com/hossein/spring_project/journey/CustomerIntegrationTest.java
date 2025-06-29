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
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.hossein.spring_project.customer.Customer;
import com.hossein.spring_project.customer.CustomerRegistrationRequest;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/v1/customers";
    @Test
    void canRegisterACustomer(){
        // create reistration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.firstName(),
                email = fakerName.lastName() + UUID.randomUUID() + "@example.com";
        int age = RANDOM.nextInt(10,90);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            name, email, age,true
        );

        // send a post request

        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();
        
        // make sure that customer is present
        Customer expectedCustomer = new Customer(
            name,email,age,true
        );

        assertThat(allCustomers)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
            .contains(expectedCustomer);
        
        // get customer by id
        int id = allCustomers.stream()
                .filter(e -> e.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
                

        expectedCustomer.setId(id);

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
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

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            name, email, age,true
        );
        // send a post request
        
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();
        

        // get customer by id
        int id = allCustomers.stream()
                .filter(e -> e.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete cusotmer

        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
        
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
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
                name, email, age,true
        );
        // send a post request
        
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();
        

        // get customer by id
        int id = allCustomers.stream()
                .filter(e -> e.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer

        Customer update = new Customer(
                fakerName.name(),fakerName.lastName() + UUID.randomUUID() + "@example.com",age + 11,false
        );

        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(update), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        
        Customer returnedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();
        update.setId(id);
        assertThat(returnedCustomer).isEqualTo(update);
    }
}
