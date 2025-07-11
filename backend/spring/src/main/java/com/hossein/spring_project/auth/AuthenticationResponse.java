package com.hossein.spring_project.auth;

import com.hossein.spring_project.customer.CustomerDTO;

public record AuthenticationResponse(
    CustomerDTO user,
    String token
) {

}
