package dev.nimbler.compiler.util.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface ListenerParser<LISTENER> {

	Collection<ParseError> parse(String string, LISTENER listener, ParseLogger parseLogger);

	Collection<ParseError> parse(InputStream stream, LISTENER listener, String file, ParseLogger parseLogger) throws IOException;

}
