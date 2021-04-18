package dev.nimbler.compiler.model.common.passes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jutils.parse.ParserException;

import dev.nimbler.compiler.util.parse.ParseError;
import dev.nimbler.compiler.util.parse.ParseLogger;
import dev.nimbler.compiler.util.parse.ParsedFile;

public final class FileParsePass<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
		extends FilePass<FileParsePassInput<COMPILATION_UNIT>, PARSED_FILE> {

	private final CompilerModel<COMPILATION_UNIT, PARSED_FILE> model;

	public FileParsePass(CompilerModel<COMPILATION_UNIT, PARSED_FILE> model) {

		Objects.requireNonNull(model);
		
		this.model = model;
	}

	@Override
	public PARSED_FILE execute(FileParsePassInput<COMPILATION_UNIT> input) throws IOException, ParserException {
		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream printStream = new PrintStream(baos);
		
		final List<ParseError> errors = new ArrayList<>();
		
		final COMPILATION_UNIT compilationUnit = input.getParser().parse(
				input.getInputStream(),
				input.getCharset(),
				errors,
				input.getFile().getParseContextName(),
				fullContextProvider -> new ParseLogger(printStream, fullContextProvider));
		
		final PARSED_FILE parsedFile = model.createParsedFile(
				input.getFile(),
				compilationUnit,
				errors.stream().map(Function.identity()).collect(Collectors.toList()),
				new String(baos.toByteArray()));
		
		return parsedFile;
	}
}
