package com.neaterbits.compiler.util.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface Parser<T, LISTENER> {

	T parse(String string);

	T parse(String string, boolean log);

	T parse(InputStream stream, String file) throws IOException;

	T parse(String string, Collection<ParseError> errors, ParseLogger parseLogger);

	T parse(InputStream stream, Collection<ParseError> errors, String file, ParseLogger parseLogger) throws IOException;

	Collection<ParseError> parse(String string, LISTENER listener, ParseLogger parseLogger);

	Collection<ParseError> parse(InputStream stream, LISTENER listener, String file, ParseLogger parseLogger) throws IOException;

}
