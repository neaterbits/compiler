package com.neaterbits.compiler.util.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import com.neaterbits.util.parse.ParserException;

public interface Parser<T> {

	T parse(String string) throws ParserException;

	T parse(String string, boolean log) throws ParserException;

	T parse(InputStream stream, Charset charSet, String file)
	            throws IOException, ParserException;

	T parse(String string, Collection<ParseError> errors, ParseLogger parseLogger)
	            throws ParserException;

	T parse(
	        InputStream stream,
	        Charset charset,
	        Collection<ParseError> errors,
	        String file,
	        ParseLogger parseLogger)
	                
                throws IOException, ParserException;

}
