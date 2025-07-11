package com.hossein.spring_project.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        String password,
        Integer age,
        Boolean gender
) {
}