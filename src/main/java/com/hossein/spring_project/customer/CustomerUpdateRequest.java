package com.hossein.spring_project.customer;

public record CustomerUpdateRequest(
    String name,
    String email,
    Integer age
) {
}
