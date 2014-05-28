package com.fixtures.exception;

public class FixtureDataNotFoundException extends RuntimeException {

	private String message;

	public FixtureDataNotFoundException(String message) {
		super(message);
		this.message = message;
	}

}
