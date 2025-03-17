package com.hossein.spring_project.ui.controllers;

public record CustomerUpdateRequest(
    String name,
    String email,
    Integer age
) {
}
