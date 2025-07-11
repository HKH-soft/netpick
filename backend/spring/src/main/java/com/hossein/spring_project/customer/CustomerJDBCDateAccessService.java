package com.hossein.spring_project.customer;

import java.util.List;
import java.util.Optional;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class CustomerJDBCDateAccessService implements CustomerDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;
    
    public CustomerJDBCDateAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, password, age, gender
                FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        var sql = (
		"""
                SELECT id, name, email, password, age, gender
                FROM customer
                WHERE id = ?
                """);
        
        return jdbcTemplate
                .query(sql,customerRowMapper,customerId)
                .stream()
                .findFirst();
    }

    @Override
    public void createCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, password, age, gender)
                VALUES(?, ?, ?, ?, ?);
                """;
        int result = jdbcTemplate.update(
            sql,
            customer.getName(),
            customer.getEmail(),
            customer.getPassword(),
            customer.getAge(),
            customer.getGender()
            );
        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer 
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        var sql = """
                SELECT count(id)
                FROM customer 
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void removeCustomer(Integer id) {
        var sql = """
                DELETE 
                FROM customer
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, id);
        System.out.println("delete customer by id result = " + result);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() != null){
            String sql = "UPDATE customer  SET name = ? WHERE  id = ?";
            int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getId()
            );
            System.out.println("update customer name result = " + result);
        }

        if(customer.getAge() != null){
            String sql = "UPDATE customer  SET age = ? WHERE  id = ?";
            int result = jdbcTemplate.update(
                sql,
                customer.getAge(),
                customer.getId()
            );
            System.out.println("update customer age result = " + result);
        }

        if(customer.getEmail() != null){
            String sql = "UPDATE customer  SET email = ? WHERE  id = ?";
            int result = jdbcTemplate.update(
                sql,
                customer.getEmail(),
                customer.getId()
            );
            System.out.println("update customer mail result = " + result);
        }
        
        if(customer.getGender() != null){
            String sql = "UPDATE customer  SET gender = ? WHERE  id = ?";
            int result = jdbcTemplate.update(
                sql,
                customer.getGender(),
                customer.getId()
            );
            System.out.println("update customer gender result = " + result);
        }
    }

    @Override
    public Optional<Customer> selectCustomerByEmail(String email) {
        var sql = (
		"""
                SELECT id, name, email, password, age, gender
                FROM customer
                WHERE email = ?
                """);
        
        return jdbcTemplate
                .query(sql,customerRowMapper,email)
                .stream()
                .findFirst();
    }
}
