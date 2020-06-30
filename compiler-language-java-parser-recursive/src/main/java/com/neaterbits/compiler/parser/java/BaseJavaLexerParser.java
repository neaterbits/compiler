package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.BaseLexerParser;
import com.neaterbits.compiler.parser.recursive.NamesList;
import com.neaterbits.compiler.parser.recursive.ProcessParts;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class BaseJavaLexerParser<COMPILATION_UNIT> extends BaseLexerParser<JavaToken> {

    final IterativeParserListener<COMPILATION_UNIT> listener;
    final JavaListenerHelper<COMPILATION_UNIT> listenerHelper;

    BaseJavaLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        super(file, lexer, tokenizer);

        Objects.requireNonNull(listener);
        
        this.listener = listener;
        this.listenerHelper = new JavaListenerHelper<>(listener, this::writeContext);
    }
    
    final int writeCurContext() {
        
        return listener.writeContext(getLexerContext());
    }

    final int writeContext(int otherContext) {
        
        return listener.writeContext(otherContext);
    }

    final long getStringRef() {
        return lexer.getStringRef(0, 0);
    }

    final void parseNameListUntilOtherToken(
            int identifierContext,
            long identifier,
            ProcessParts<Names> processNameParts) throws IOException, ParserException {
        
        final NamesList scratch = startScratchNameParts();

        scratch.add(identifierContext, identifier);
        
        parseNames(scratch, processNameParts);
    }

    final void parseNameListUntilOtherToken(ProcessParts<Names> processNameParts) throws IOException, ParserException {
        
        final NamesList scratch = startScratchNameParts();

        parseNames(scratch, processNameParts);
    }

    private void parseNames(NamesList scratch, ProcessParts<Names> processNameParts) throws IOException, ParserException {
        
        parseNames(scratch);
        
        // reach non-namepart so process now
        scratch.complete(processNameParts);
    }
    
    final void parseNames(NamesList scratch) throws IOException, ParserException {
        
        for (;;) {

            final JavaToken partToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (partToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            scratch.add(writeCurContext(), getStringRef());
            
            final JavaToken endOfScopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
            
            if (endOfScopeToken != JavaToken.PERIOD) {
                break;
            }
        }
    }
}
