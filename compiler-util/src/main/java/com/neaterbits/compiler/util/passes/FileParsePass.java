package com.neaterbits.compiler.util.passes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class FileParsePass<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
		extends FilePass<FileParsePassInput<COMPILATION_UNIT>, PARSED_FILE> {

	private final CreateParsedFile<COMPILATION_UNIT, PARSED_FILE> makeParsedFile;

	public FileParsePass(
			CreateParsedFile<COMPILATION_UNIT, PARSED_FILE> makeParsedFile,
			Function<PARSED_FILE, FileSpec> getFileSpec) {

		Objects.requireNonNull(makeParsedFile);
		Objects.requireNonNull(getFileSpec);
		
		this.makeParsedFile = makeParsedFile;
	}

	@Override
	public PARSED_FILE execute(FileParsePassInput<COMPILATION_UNIT> input) throws IOException {

		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream printStream = new PrintStream(baos);
		final ParseLogger parseLogger = new ParseLogger(printStream);
		
		final List<ParseError> errors = new ArrayList<>();
		
		final COMPILATION_UNIT compilationUnit = input.getParser().parse(
				input.getInputStream(),
				input.getCharset(),
				errors,
				input.getFile().getParseContextName(),
				parseLogger);
		
		final PARSED_FILE parsedFile = makeParsedFile.create(
				input.getFile(),
				compilationUnit,
				errors.stream().map(Function.identity()).collect(Collectors.toList()),
				new String(baos.toByteArray()));
		
		return parsedFile;
	}
}
