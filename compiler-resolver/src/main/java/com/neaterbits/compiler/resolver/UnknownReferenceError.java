package com.neaterbits.compiler.resolver;

import java.util.Objects;

public final class UnknownReferenceError extends ResolveError {

	private final String message;

	public UnknownReferenceError(String message) {
	    
	    Objects.requireNonNull(message);
	    
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
