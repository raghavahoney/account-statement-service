package com.nagarro.account.statement.exceptions;

public class LoginExistsException extends RuntimeException {

	public LoginExistsException(String msg) {
		super(msg);
	}

}
