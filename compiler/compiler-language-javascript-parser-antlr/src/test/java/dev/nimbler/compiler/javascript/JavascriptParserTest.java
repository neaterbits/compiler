package dev.nimbler.compiler.javascript;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jutils.parse.ParserException;

import dev.nimbler.compiler.javascript.parser.antlr4.JavascriptAntlrParser;
import dev.nimbler.compiler.util.parse.ParseError;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class JavascriptParserTest {

	@Test
	public void testParser() throws ParserException {
		final String source = 
			"function xyz() {\n" +
		    "}\n";
		
		final JavascriptAntlrParser parser = new JavascriptAntlrParser(true);

		final List<ParseError> errors = new ArrayList<>();
		
		parser.parse(
		        source,
		        errors,
		        fullContextProvider -> new ParseLogger(System.out, fullContextProvider));
		
		assertThat(errors.isEmpty()).isTrue();
	}
}
