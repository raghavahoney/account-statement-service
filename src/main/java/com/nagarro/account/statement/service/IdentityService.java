package com.nagarro.account.statement.service;

import com.nagarro.account.statement.model.AuthenticationRequest;
import com.nagarro.account.statement.model.AuthenticationResponse;
import org.springframework.security.authentication.BadCredentialsException;

public interface IdentityService {
    AuthenticationResponse login(AuthenticationRequest request) throws BadCredentialsException;
    void logout();
}
