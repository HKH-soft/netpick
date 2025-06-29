package com.hossein.spring_project.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hossein.spring_project.AbstractTestcontainers;

class CustomerJDBCDateAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDateAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDateAccessService(
            getJdbcTemplate(),
            customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {

        Customer customer = new Customer(
            faker.name().fullName(),
            faker.internet().emailAddress() + "-" + UUID.randomUUID(),
            20,
            true);
        underTest.createCustomer(customer);

        List<Customer> actual = underTest.selectAllCustomers();

        assertThat(actual).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
        
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true);
        underTest.createCustomer(customer);
        Integer id = underTest.selectAllCustomers()
                        .stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);
    
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });

    }

    @Test
    void willReturnEmptyWhenSelectCustomerById(){
        int id = -1;

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();
    }

    @Test
    void createCustomer() {

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true);

        underTest.createCustomer(customer);
        Optional<Customer> actual = underTest.selectAllCustomers()
                        .stream()
                        .filter(c -> c.getEmail().equals(email))
                        .findFirst();
        
        assertThat(actual).hasValueSatisfying(c -> {
                assertThat(c.getId()).isGreaterThan(0);
                assertThat(c.getName()).isEqualTo(customer.getName());
                assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                assertThat(c.getAge()).isEqualTo(customer.getAge());
                assertThat(c.getGender()).isEqualTo(customer.getGender());
        });


    }


    @Test
    void existsPersonWithEmail() {

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
        );
        underTest.createCustomer(customer);

        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isTrue();


    }

    @Test
    void whenThereIsNoEmailExistsPersonWithEmail(){

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();

        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isFalse();

    }

    @Test
    void existsPersonWithId() {

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
        );
        underTest.createCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                        .stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();
        boolean actual = underTest.existsPersonWithId(id);

        assertThat(actual).isTrue();
                            

    }

    @Test
    void personWithIdDoesNotExists(){

        Integer id = -1;

        boolean actual = underTest.existsPersonWithId(id);

        assertThat(actual).isFalse();

    }

    @Test
    void removeCustomer() {

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
        );
        underTest.createCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                        .stream()
                        .filter(c -> c.getEmail().equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();

        underTest.removeCustomer(id);

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isEmpty();

    }

    @Test
    void updateCustomer() {

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
            );
        String newName = "hosi";
        underTest.createCustomer(customer);
        Integer id = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
        
        customer.setId(id);
        customer.setName(newName);

        underTest.updateCustomer(customer);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });

    }
    
    @Test
    void updateCustomerEmail(){
        
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
            );
        String newEmail = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        underTest.createCustomer(customer);
        Integer id = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
        
        customer.setId(id);
        customer.setEmail(newEmail);
        
        underTest.updateCustomer(customer);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });

    }
    @Test
    void updateCustomerAge(){
        
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
            );
        Integer newAge = 11 ;
        underTest.createCustomer(customer);
        Integer id = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
        
        customer.setId(id);
        customer.setAge(newAge);
        
        underTest.updateCustomer(customer);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });

    }
    @Test
    void updateCustomerGender(){
        
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
            );
        underTest.createCustomer(customer);
        Integer id = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
        
        customer.setId(id);
        customer.setGender(false);
        
        underTest.updateCustomer(customer);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });

    }

    @Test
    void willUpdateEverthing(){
        
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
            );
        underTest.createCustomer(customer);
        Integer id = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
            
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(54);
        update.setName("hos");
        update.setGender(false);
        
        underTest.updateCustomer(update);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        
        assertThat(actual).isPresent().hasValue(update);

    }

    @Test 
    void willNotUpdateWhenNothingToUpdate(){

        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
            faker.name().fullName(),
            email,
            20,
            true
            );
        underTest.createCustomer(customer);

        Integer id = underTest.selectAllCustomers()
            .stream()
            .filter(c -> c.getEmail().equals(email))
            .map(Customer::getId)
            .findFirst()
            .orElseThrow();
            
        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });

    }

}
