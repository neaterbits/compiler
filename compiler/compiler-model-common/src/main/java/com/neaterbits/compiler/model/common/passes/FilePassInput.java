package com.neaterbits.compiler.model.common.passes;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;

public class FilePassInput {
	
	private final InputStream inputStream;
	private final Charset charset;
	private final FileSpec file;
	
	public FilePassInput(InputStream inputStream, Charset charset, FileSpec file) {

		Objects.requireNonNull(inputStream);
		Objects.requireNonNull(charset);
		Objects.requireNonNull(file);
		
		this.inputStream = inputStream;
		this.charset = charset;
		this.file = file;
	}

	public final InputStream getInputStream() {
		return inputStream;
	}

	public final Charset getCharset() {
		return charset;
	}

	public final FileSpec getFile() {
		return file;
	}
}
