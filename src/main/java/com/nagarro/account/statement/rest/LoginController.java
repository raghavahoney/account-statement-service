package com.nagarro.account.statement.rest;

import com.nagarro.account.statement.model.AuthenticationRequest;
import com.nagarro.account.statement.model.AuthenticationResponse;
import com.nagarro.account.statement.service.IdentityService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin
@Slf4j
@AllArgsConstructor
@Api(tags = "Authentication Service")
public class LoginController {

    private final IdentityService identityServiceImpl;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> generateAuthenticationToken(@RequestBody @Valid AuthenticationRequest authenticationRequest)
            throws BadCredentialsException {
        return ResponseEntity.ok(identityServiceImpl.login(authenticationRequest));
    }

    @PostMapping(value = "/user/logout")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Void> logout() {
        identityServiceImpl.logout();
        return ResponseEntity.ok().build();
    }

}
