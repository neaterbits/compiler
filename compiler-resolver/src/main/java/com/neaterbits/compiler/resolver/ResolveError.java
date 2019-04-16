package com.neaterbits.compiler.resolver;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.CompileError;

public final class ResolveError extends CompileError {

	private final String message;
	
	public ResolveError(String message) {

		Objects.requireNonNull(message);
		
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
