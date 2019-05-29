package com.neaterbits.compiler.util.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

public interface Parser<T> {

	T parse(String string);

	T parse(String string, boolean log);

	T parse(InputStream stream, Charset charset, String file) throws IOException;

	T parse(String string, Collection<ParseError> errors, ParseLogger parseLogger);

	T parse(InputStream stream, Charset charset, Collection<ParseError> errors, String file, ParseLogger parseLogger) throws IOException;

}
