package com.neaterbits.compiler.model.common.passes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.util.parse.ParserException;

public final class FileParsePass<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
		extends FilePass<FileParsePassInput<COMPILATION_UNIT>, PARSED_FILE> {

	private final CompilerModel<COMPILATION_UNIT, PARSED_FILE> model;
	private final FullContextProvider fullContextProvider;

	public FileParsePass(
	        CompilerModel<COMPILATION_UNIT, PARSED_FILE> model,
			FullContextProvider fullContextProvider) {

		Objects.requireNonNull(model);
		Objects.requireNonNull(fullContextProvider);
		
		this.model = model;
		this.fullContextProvider = fullContextProvider;
	}

	@Override
	public PARSED_FILE execute(FileParsePassInput<COMPILATION_UNIT> input) throws IOException, ParserException {
		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream printStream = new PrintStream(baos);
		final ParseLogger parseLogger = new ParseLogger(printStream, fullContextProvider);
		
		final List<ParseError> errors = new ArrayList<>();
		
		final COMPILATION_UNIT compilationUnit = input.getParser().parse(
				input.getInputStream(),
				input.getCharset(),
				errors,
				input.getFile().getParseContextName(),
				parseLogger);
		
		final PARSED_FILE parsedFile = model.createParsedFile(
				input.getFile(),
				compilationUnit,
				errors.stream().map(Function.identity()).collect(Collectors.toList()),
				new String(baos.toByteArray()));
		
		return parsedFile;
	}
}
