package com.nomsic.pconfig.util;

public class SerializationException extends Exception {

	private static final long serialVersionUID = 6810026452073824433L;

	public SerializationException() {
		super();
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(Throwable cause) {
		super(cause);
	}
}
