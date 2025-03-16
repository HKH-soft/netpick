package com.hossein.spring_project.ui.controllers;

public record CustomerRegistrationRequest(
    String name,
    String email,
    Integer age
) {
}
