package dev.nimbler.compiler.parser.java.recursive;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.neaterbits.util.io.buffers.StringBuffers;
import com.neaterbits.util.io.loadstream.SimpleLoadStream;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.parser.listener.common.CreateParserListener;
import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;

public class JavaRecursiveParserHelper<COMPILATION_UNIT> {
    
    private final CreateParserListener<COMPILATION_UNIT> createListener;
    
    public JavaRecursiveParserHelper(CreateParserListener<COMPILATION_UNIT> createListener) {
        
        Objects.requireNonNull(createListener);
        
        this.createListener = createListener;
    }
    
    public final COMPILATION_UNIT parse(String file, InputStream inputStream) throws IOException, ParserException {

        Objects.requireNonNull(inputStream);
        
        final StringBuffers stringBuffers = new StringBuffers(new SimpleLoadStream(inputStream));
        
        final IterativeParseTreeListener<COMPILATION_UNIT> listener
            = createListener.createParserListener(file, stringBuffers);

        final Lexer<JavaToken, CharInput> lexer = new Lexer<>(
                stringBuffers,
                JavaToken.class,
                JavaToken.NONE,
                JavaToken.EOF,
                new JavaToken[] { JavaToken.WS },
                new JavaToken[] { JavaToken.C_COMMENT, JavaToken.CPP_COMMENT });
     
        final JavaLexerParser<COMPILATION_UNIT> lexerParser
            = new JavaLexerParser<>(file, lexer, stringBuffers, listener);
        
        return lexerParser.parse();
    }
}
