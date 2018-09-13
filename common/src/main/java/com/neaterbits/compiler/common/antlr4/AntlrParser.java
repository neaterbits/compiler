package com.neaterbits.compiler.common.antlr4;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface AntlrParser<T, LISTENER extends ModelParserListener<T>> {

	T parse(String string);

	T parse(InputStream stream) throws IOException;

	T parse(String string, Collection<AntlrError> errors);

	T parse(InputStream stream, Collection<AntlrError> errors) throws IOException;

	Collection<AntlrError> parse(String string, LISTENER listener);

	Collection<AntlrError> parse(InputStream stream, LISTENER listener) throws IOException;

}
