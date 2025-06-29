package com.hossein.spring_project.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.hossein.spring_project.AbstractTestcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest extends AbstractTestcontainers{
    
    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp(){
        underTest.deleteAll();
    }

    @Test
    void testExistsCustomerByEmail() {

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
        );
        underTest.save(customer);

        boolean actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isTrue();

    }

    @Test
    void whenThereIsNoEmailExistsPersonWithEmail(){

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();

        boolean actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isFalse();

    }


    @Test
    void testExistsCustomerById() {

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
        );
        underTest.save(customer);

        Integer id = underTest.findAll()
                        .stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();
        boolean actual = underTest.existsCustomerById(id);

        assertThat(actual).isTrue();
            

    }

    @Test
    void testExistsCustomerByIdFailsWhenIdNotPressent(){
        int id = -1;

        boolean actual = underTest.existsCustomerById(id);

        assertThat(actual).isFalse();
    }
}
