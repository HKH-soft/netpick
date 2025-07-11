package com.hossein.spring_project.customer;

import java.util.List;

public record CustomerDTO(
    Integer id,
    String name,
    String email,
    boolean gender,
    Integer age,
    List<String> roles,
    String username
) {
} 
