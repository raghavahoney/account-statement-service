package com.nagarro.account.statement.config;

import com.nagarro.account.statement.exceptions.BadRequestParamterException;
import com.nagarro.account.statement.exceptions.LoginExistsException;
import com.nagarro.account.statement.model.ErrorDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = LoginExistsException.class)
    protected ResponseEntity<ErrorDetail> handleExistingLoginException(LoginExistsException ex) {

        return new ResponseEntity<>(
                ErrorDetail.builder().errorCode("ERR_001").errorMessage(ex.getMessage()).build(),
                getHeaders(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BadRequestParamterException.class)
    protected ResponseEntity<ErrorDetail> handleBadRequestParamsException(BadRequestParamterException ex) {

        return new ResponseEntity<>(
                ErrorDetail.builder().errorCode("ERR_002").errorMessage(ex.getMessage()).build(),
                getHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadCredentialsException.class, UsernameNotFoundException.class})
    protected ResponseEntity<ErrorDetail> handleBadCredentials(BadCredentialsException ex) {

        return new ResponseEntity<>(
                ErrorDetail.builder().errorCode("ERR_003").errorMessage(ex.getMessage()).build(),
                getHeaders(),
                HttpStatus.UNAUTHORIZED);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
