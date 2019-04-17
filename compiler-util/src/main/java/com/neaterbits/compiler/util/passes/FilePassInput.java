package com.neaterbits.compiler.util.passes;

import java.io.InputStream;
import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;

public class FilePassInput {
	
	private final InputStream inputStream;
	private final FileSpec file;
	
	public FilePassInput(InputStream inputStream, FileSpec file) {

		Objects.requireNonNull(inputStream);
		Objects.requireNonNull(file);
		
		this.inputStream = inputStream;
		this.file = file;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public FileSpec getFile() {
		return file;
	}
}
