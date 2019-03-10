package com.neaterbits.compiler.javascript;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.neaterbits.compiler.antlr4.AntlrError;
import com.neaterbits.compiler.javascript.parser.antlr4.JavascriptAntlrParser;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class JavascriptParserTest {

	@Test
	public void testParser() {
		final String source = 
			"function xyz() {\n" +
		    "}\n";
		
		final JavascriptAntlrParser parser = new JavascriptAntlrParser(true);

		final List<AntlrError> errors = new ArrayList<>();
		
		parser.parse(source, errors, new ParseLogger(System.out));
		
		assertThat(errors.isEmpty()).isTrue();
	}
}
