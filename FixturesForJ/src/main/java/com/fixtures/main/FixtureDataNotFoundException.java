package com.fixtures.main;

public class FixtureDataNotFoundException extends RuntimeException {

	private String message;

	public FixtureDataNotFoundException(String message) {
		super(message);
		this.message = message;
	}

}
