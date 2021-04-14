package dev.nimbler.compiler.language.java;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.util.io.strings.StringSourceInputStream;
import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.parser.java.recursive.JavaRecursiveParserHelper;
import dev.nimbler.compiler.parser.listener.common.CreateParserListener;
import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.parser.recursive.InputLexerParser;
import dev.nimbler.compiler.util.parse.ParseError;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
            ParseLogger parseLogger)
                        throws IOException, ParserException {

        final JavaRecursiveParserHelper<COMPILATION_UNIT> parser
            = new JavaRecursiveParserHelper<>(createListener);
        
        return parser.parse(file, stream);
    }
}
