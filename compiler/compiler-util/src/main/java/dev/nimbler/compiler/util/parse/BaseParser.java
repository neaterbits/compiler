package dev.nimbler.compiler.util.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.io.strings.StringSourceInputStream;
import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.StringSourceFullContextProvider;

public abstract class BaseParser<T, LISTENER> implements Parser<T> {

    protected abstract T parse(StringSourceInputStream stream, Collection<ParseError> errors, String file, ParseLogger parseLogger)
                throws IOException, ParserException;
    
    @Override
    public final T parse(String string) throws ParserException {
        return parse(string, true);
    }
    
    private static ParseLogger makeParseLogger(String file, StringSource stringSource) {
        
        return makeParseLogger(
                file,
                stringSource,
                fullContextProvider -> new ParseLogger(System.out, fullContextProvider));
    }

    private static ParseLogger makeParseLogger(String file, StringSource stringSource, CreateParseLogger createParseLogger) {
        
        return createParseLogger.create(new StringSourceFullContextProvider(file, stringSource));
    }

    @Override
    public final T parse(String string, boolean log) throws ParserException {
        
        final StringSourceInputStream stringSource = StringSourceInputStream.fromString(string);
        
        try {
            return parse(
                    stringSource,
                    new ArrayList<>(),
                    null,
                    !log ? null : makeParseLogger(null, stringSource));
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public final T parse(InputStream stream, Charset charSet, String file) throws IOException, ParserException {
        
        final StringSourceInputStream stringSource = new StringSourceInputStream(stream, charSet);
        
        return parse(stringSource, new ArrayList<>(), file, makeParseLogger(file, stringSource));
    }

    @Override
    public final T parse(
            String string,
            Collection<ParseError> errors,
            CreateParseLogger createParseLogger) throws ParserException {
        
        final StringSourceInputStream stringSource = StringSourceInputStream.fromString(string);
        
        try {
            return parse(
                    stringSource,
                    errors,
                    null,
                    makeParseLogger(null, stringSource, createParseLogger));
            
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public final T parse(
            InputStream stream,
            Charset charset,
            Collection<ParseError> errors,
            String file,
            CreateParseLogger createParseLogger) throws IOException, ParserException {
        
        final StringSourceInputStream stringSource = new StringSourceInputStream(stream, charset);
        
        return parse(
                stringSource,
                errors,
                file,
                makeParseLogger(file, stringSource, createParseLogger));
    }
}
