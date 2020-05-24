package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.util.io.loadstream.SimpleLoadStream;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;
import com.test.util.io.buffers.StringBuffers;

public final class JavaParser<COMPILATION_UNIT> {
    
    private final Function<StringBuffers, IterativeParserListener<COMPILATION_UNIT>> createListener;
    
    public JavaParser(Function<StringBuffers, IterativeParserListener<COMPILATION_UNIT>> createListener) {
        
        Objects.requireNonNull(createListener);
        
        this.createListener = createListener;
    }
    
    public COMPILATION_UNIT parse(String file, InputStream inputStream) throws IOException, ParserException {

        Objects.requireNonNull(inputStream);
        
        final StringBuffers stringBuffers = new StringBuffers(new SimpleLoadStream(inputStream));
        
        final IterativeParserListener<COMPILATION_UNIT> listener = createListener.apply(stringBuffers);

        Lexer<JavaToken, CharInput> lexer = new Lexer<>(
                stringBuffers,
                JavaToken.class,
                JavaToken.NONE,
                JavaToken.EOF,
                new JavaToken[] { JavaToken.WS },
                new JavaToken[] { JavaToken.C_COMMENT, JavaToken.CPP_COMMENT });
     
        final JavaLexerParser<COMPILATION_UNIT> lexerParser = new JavaLexerParser<>(file, lexer, stringBuffers, listener);
        
        return lexerParser.parse();
    }
}
