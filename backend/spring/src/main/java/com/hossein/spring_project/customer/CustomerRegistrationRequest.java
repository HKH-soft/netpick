package com.hossein.spring_project.customer;

public record CustomerRegistrationRequest(
    String name,
    String email,
    Integer age,
    Boolean gender
) {
}
