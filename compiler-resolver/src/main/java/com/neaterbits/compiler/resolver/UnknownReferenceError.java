package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.util.parse.CompileError;

public class UnknownReferenceError extends CompileError {

	private final String message;

	public UnknownReferenceError(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
