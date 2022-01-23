package com.nagarro.account.statement.service.impl;

import com.nagarro.account.statement.exceptions.GenericRuntimeException;
import com.nagarro.account.statement.exceptions.LoginExistsException;
import com.nagarro.account.statement.model.AuthenticationRequest;
import com.nagarro.account.statement.model.AuthenticationResponse;
import com.nagarro.account.statement.model.IdentityAttributes;
import com.nagarro.account.statement.repository.IdentityRepository;
import com.nagarro.account.statement.service.IdentityService;
import com.nagarro.account.statement.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdentityServiceImpl implements IdentityService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsServiceImpl;
    private final IdentityRepository identityRepository;

    private static final String ERROR_MSG_1 = "Unable to reach redis cache db";

    @Value("${token.validity}")
    private Long tokenValidityInMins;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request)  {

        checkIfUserSessionExsits(request);
        authenticate(request.getUsername(), request.getPassword());

        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        saveUserLoginSession(request.getUsername(),token);
        return AuthenticationResponse.builder().token(token).build();
    }

    @Override
    public void logout() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();

        log.info("Logging out user {}",userName);
        Optional<IdentityAttributes> identityAttributesOptional;
        IdentityAttributes identityAttributes = IdentityAttributes.builder().build();
        try {
            identityAttributesOptional = identityRepository.findById(userName);
            if(identityAttributesOptional.isPresent()){
                identityAttributes = identityAttributesOptional.get();
            }
        } catch (Exception e) {
            log.error(ERROR_MSG_1, e);
            throw new GenericRuntimeException("Exception occured while reaching redis cached DB");
        }
        identityRepository.delete(identityAttributes);
    }

    private void authenticate(String username, String password) throws BadCredentialsException {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }

    private void checkIfUserSessionExsits(AuthenticationRequest request){

        Optional<IdentityAttributes> identityAttributesOptional;
        try {
            identityAttributesOptional = identityRepository.findById(request.getUsername());
        } catch (Exception e) {
            log.error(ERROR_MSG_1, e);
            throw new GenericRuntimeException("Exception occured while reaching redis cached DB");
        }

        if (identityAttributesOptional.isPresent()) {
            IdentityAttributes representation = identityAttributesOptional.get();
            if (!representation.getToken().isEmpty()) {
                log.error("Active login session exists for user [{}]. Need to logout before login", request.getUsername());
                throw new LoginExistsException("Active login session exists for user ["+request.getUsername()+"]. Need to logout before login");
            }

        }
    }

    private void saveUserLoginSession(String userName, String token){
        IdentityAttributes identityAttributes = IdentityAttributes.builder().
                authId(userName).
                token(token).
                validity(tokenValidityInMins).build();
        try {
            log.info("Saving Token in Redis for {}",userName);
            identityRepository.save(identityAttributes);
        } catch (Exception e) {
            log.error(ERROR_MSG_1, e);
            throw new GenericRuntimeException("Exception occured while reaching redis cached DB. Unable to save token");
        }


    }
}
