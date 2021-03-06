package dev.nimbler.compiler.model.common.passes;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.parse.Parser;

public final class FileParsePassInput<COMPILATION_UNIT> extends FilePassInput {

	private final Parser<COMPILATION_UNIT> parser;

	public FileParsePassInput(InputStream inputStream, Charset charset, FileSpec file, Parser<COMPILATION_UNIT> parser) {
		super(inputStream, charset, file);
	
		Objects.requireNonNull(parser);
		
		this.parser = parser;
	}

	Parser<COMPILATION_UNIT> getParser() {
		return parser;
	}
}
