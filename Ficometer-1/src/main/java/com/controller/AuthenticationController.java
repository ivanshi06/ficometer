package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.LoginCustomerDto;
import com.dto.LoginResponse;
import com.dto.RegisterCustomerDto;
import com.model.Customer;
import com.service.CustomerService2;
import com.service.JwtService;

@RestController
@RequestMapping("/customer")
public class AuthenticationController {
    private final JwtService jwtService;
    private final CustomerService2 authenticationService;
    public AuthenticationController(JwtService jwtService, CustomerService2 authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }
    @PostMapping("/signup")
    public ResponseEntity<Customer> register(@RequestBody Customer registerCustomerDto) {
    	System.out.println(registerCustomerDto);
        Customer registeredUser = authenticationService.signup(registerCustomerDto);
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/login")
    public Object authenticate(@RequestBody LoginCustomerDto loginUserDto) {
    	System.out.println("In authenticate ");
    	Customer user = authenticationService.findByUsername(loginUserDto.getEmail());
//        UserDetails user = authenticationService.findByUsername(currentUser.getUsername()); 
    	System.out.println(user);
//        if(!user.isEmailVerified()) {
//        	System.out.println("User not verified");
//        	return ResponseEntity.badRequest();
//        }
        Customer authenticatedUser = authenticationService.authenticate(loginUserDto);
        System.out.println(authenticatedUser);
        System.out.println("______________");
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
}
