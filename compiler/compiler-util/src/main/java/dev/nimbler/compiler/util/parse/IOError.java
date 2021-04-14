package dev.nimbler.compiler.util.parse;

import java.io.File;
import java.util.Objects;

public final class IOError extends CompileError {

	private final File file;
	private final String message;
	private final Exception exception;

	public IOError(File file, String message, Exception exception) {
		
		Objects.requireNonNull(file);
		Objects.requireNonNull(message);
		
		this.file = file;
		this.message = message;
		this.exception = exception;
	}

	public File getFile() {
		return file;
	}

	public String getMessage() {
		return message;
	}

	public Exception getException() {
		return exception;
	}
}
