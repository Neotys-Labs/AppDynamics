package com.neotys.appdynamics;

public class AppDynamicsException extends Exception {

	public AppDynamicsException(final String message) {
		super(message);
	}

	public AppDynamicsException(final Exception exception) {
		super(exception);
	}
}
