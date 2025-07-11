package com.hossein.spring_project.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hossein.spring_project.customer.Customer;
import com.hossein.spring_project.customer.CustomerDTO;
import com.hossein.spring_project.customer.CustomerDTOMapper;
import com.hossein.spring_project.jwt.JWTUtil;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final CustomerDTOMapper customerDTOMapper;

    public AuthenticationService(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
            CustomerDTOMapper customerDTOMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customerDTOMapper = customerDTOMapper;
    }

    public AuthenticationResponse login(AuthenticationRequest request){
        Authentication authenticationRsponse = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
        CustomerDTO customer = customerDTOMapper.apply((Customer) authenticationRsponse.getPrincipal());
        String token = jwtUtil.issueToken(customer.username(),customer.roles());
        return new AuthenticationResponse(customer,token);
    }

}
