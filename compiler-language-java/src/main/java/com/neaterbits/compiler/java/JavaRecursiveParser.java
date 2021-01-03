package com.neaterbits.compiler.java;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.parser.java.JavaRecursiveParserHelper;
import com.neaterbits.compiler.parser.listener.common.CreateParserListener;
import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.parser.recursive.InputLexerParser;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSourceInputStream;
import com.neaterbits.util.parse.ParserException;

public final class JavaRecursiveParser<COMPILATION_UNIT>
    extends InputLexerParser<COMPILATION_UNIT, IterativeParseTreeListener<COMPILATION_UNIT>> {

    private final CreateParserListener<COMPILATION_UNIT> createListener;
        
    public JavaRecursiveParser(CreateParserListener<COMPILATION_UNIT> createListener) {

        Objects.requireNonNull(createListener);
        
        this.createListener = createListener;
    }

    @Override
    protected final COMPILATION_UNIT parse(
            StringSourceInputStream stream,
            Collection<ParseError> errors,
            String file,
            ParseLogger parseLogger) throws IOException, ParserException {

        final JavaRecursiveParserHelper<COMPILATION_UNIT> parser = new JavaRecursiveParserHelper<>(createListener);
        
        return parser.parse(file, stream);
    }
}
