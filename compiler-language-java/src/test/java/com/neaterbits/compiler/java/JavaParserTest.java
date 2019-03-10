package com.neaterbits.compiler.java;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.neaterbits.compiler.antlr4.AntlrError;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;
import com.neaterbits.compiler.util.parse.ParseLogger;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaParserTest {

	@Test
	public void testParser() {
		final String source = 
			"package com.test;\n" +
			"class TestClass {\n" +
		    "}\n";
		
		final Java8AntlrParser parser = new Java8AntlrParser(true);

		final List<AntlrError> errors = new ArrayList<>();
		
		parser.parse(source, errors, new ParseLogger(System.out));
		
		assertThat(errors.isEmpty()).isTrue();
	}
}
