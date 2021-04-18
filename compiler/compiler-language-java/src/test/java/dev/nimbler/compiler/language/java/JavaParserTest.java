package dev.nimbler.compiler.language.java;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jutils.parse.ParserException;

import dev.nimbler.compiler.util.parse.ParseError;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class JavaParserTest {

	@Test
	public void testParser() throws ParserException {
		final String source = 
			"package com.test;\n" +
			"class TestClass {\n" +
		    "}\n";
		
		final Java8AntlrObjectModelParser parser = new Java8AntlrObjectModelParser(true);

		final List<ParseError> errors = new ArrayList<>();
		
		parser.parse(
		        source,
		        errors,
		        fullContextProvider -> new ParseLogger(System.out, fullContextProvider));
		
		assertThat(errors.isEmpty()).isTrue();
	}
}
