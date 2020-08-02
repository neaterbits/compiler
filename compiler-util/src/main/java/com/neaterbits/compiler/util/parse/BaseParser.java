package com.neaterbits.compiler.util.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import com.neaterbits.compiler.util.CastFullContextProvider;
import com.neaterbits.util.io.strings.StringSourceInputStream;
import com.neaterbits.util.parse.ParserException;

public abstract class BaseParser<T, LISTENER> implements Parser<T> {

    protected abstract T parse(StringSourceInputStream stream, Collection<ParseError> errors, String file, ParseLogger parseLogger)
                throws IOException, ParserException;
    
    @Override
    public final T parse(String string) throws ParserException {
        return parse(string, true);
    }
    
    private static ParseLogger makeParseLogger() {
        
        return new ParseLogger(System.out, CastFullContextProvider.INSTANCE);
    }
    
    @Override
    public final T parse(String string, boolean log) throws ParserException {
        try {
            return parse(
                    StringSourceInputStream.fromString(string),
                    new ArrayList<>(),
                    null,
                    !log ? null : makeParseLogger());
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public final T parse(InputStream stream, Charset charset, String file) throws IOException, ParserException {
        return parse(new StringSourceInputStream(stream, charset), new ArrayList<>(), file, makeParseLogger());
    }

    @Override
    public final T parse(String string, Collection<ParseError> errors, ParseLogger parseLogger) throws ParserException {
        
        try {
            return parse(StringSourceInputStream.fromString(string), errors, null, parseLogger);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public final T parse(InputStream stream, Charset charset, Collection<ParseError> errors, String file, ParseLogger parseLogger) throws IOException, ParserException {
        return parse(new StringSourceInputStream(stream, charset), errors, file, parseLogger);
    }
}
