package com.neaterbits.compiler.resolver;

public class UnknownReferenceError extends ResolveError {

	private final String message;

	public UnknownReferenceError(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
