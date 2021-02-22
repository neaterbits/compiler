package com.neaterbits.compiler.common.antlr4;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import com.neaterbits.compiler.common.log.ParseLogger;

public interface AntlrParser<T, LISTENER extends ModelParserListener<T>> {

	T parse(String string);

	T parse(InputStream stream, String file) throws IOException;

	T parse(String string, Collection<AntlrError> errors, ParseLogger parseLogger);

	T parse(InputStream stream, Collection<AntlrError> errors, String file, ParseLogger parseLogger) throws IOException;

	Collection<AntlrError> parse(String string, LISTENER listener, ParseLogger parseLogger);

	Collection<AntlrError> parse(InputStream stream, LISTENER listener, String file, ParseLogger parseLogger) throws IOException;

}
