package com.hossein.spring_project.auth;

public record AuthenticationRequest(
    String username,
    String password
) {

}
