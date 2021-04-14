package com.neaterbits.compiler.util.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.util.parse.ParserException;

public interface Parser<T> {

    @FunctionalInterface
    public interface CreateParseLogger {

        ParseLogger create(FullContextProvider fullContextProvider);
    }

	T parse(String string) throws ParserException;

	T parse(String string, boolean log) throws ParserException;

	T parse(InputStream stream, Charset charSet, String file)
	            throws IOException, ParserException;

	T parse(String string, Collection<ParseError> errors, CreateParseLogger createParseLogger)
	            throws ParserException;

	T parse(
	        InputStream stream,
	        Charset charset,
	        Collection<ParseError> errors,
	        String file,
	        CreateParseLogger createParseLogger)
	                
                throws IOException, ParserException;

}
