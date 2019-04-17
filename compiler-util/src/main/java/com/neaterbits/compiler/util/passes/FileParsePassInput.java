package com.neaterbits.compiler.util.passes;

import java.io.InputStream;
import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.Parser;

public final class FileParsePassInput<COMPILATION_UNIT> extends FilePassInput {

	private final Parser<COMPILATION_UNIT> parser;

	public FileParsePassInput(InputStream inputStream, FileSpec file, Parser<COMPILATION_UNIT> parser) {
		super(inputStream, file);
	
		Objects.requireNonNull(parser);
		
		this.parser = parser;
	}

	Parser<COMPILATION_UNIT> getParser() {
		return parser;
	}
}
