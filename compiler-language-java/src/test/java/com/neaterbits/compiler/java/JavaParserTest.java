package com.neaterbits.compiler.java;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.neaterbits.compiler.util.CastFullContextProvider;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.parse.ParserException;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaParserTest {

	@Test
	public void testParser() throws ParserException {
		final String source = 
			"package com.test;\n" +
			"class TestClass {\n" +
		    "}\n";
		
		final Java8AntlrObjectModelParser parser = new Java8AntlrObjectModelParser(true);

		final List<ParseError> errors = new ArrayList<>();
		
		parser.parse(source, errors, new ParseLogger(System.out, CastFullContextProvider.INSTANCE));
		
		assertThat(errors.isEmpty()).isTrue();
	}
}
