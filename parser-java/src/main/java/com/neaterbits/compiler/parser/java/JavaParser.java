package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.neaterbits.util.io.loadstream.SimpleLoadStream;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.parse.Lexer;
import com.test.util.io.buffers.StringBuffers;

public final class JavaParser<COMPILATION_UNIT> {
    
    public void parse(String file, InputStream inputStream) throws IOException, ParseException {

        Objects.requireNonNull(inputStream);
        
        final StringBuffers stringBuffers = new StringBuffers(new SimpleLoadStream(inputStream));
        
        final JavaParserListener<COMPILATION_UNIT> listener = new JavaParserListener<>(stringBuffers);

        Lexer<JavaToken, CharInput> lexer = new Lexer<>(
                stringBuffers,
                JavaToken.class,
                JavaToken.NONE,
                JavaToken.EOF,
                new JavaToken[] { JavaToken.WS },
                new JavaToken[] { JavaToken.C_COMMENT, JavaToken.CPP_COMMENT });
     
        final JavaLexerParser<COMPILATION_UNIT> lexerParser = new JavaLexerParser<>(file, lexer, listener);
        
        lexerParser.parse();
    }
}
