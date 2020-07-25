package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaTypesLexerParser<COMPILATION_UNIT> extends JavaAnnotationLexerParser<COMPILATION_UNIT> {

    abstract void tryParseGenericTypeArguments() throws IOException, ParserException;
    
    JavaTypesLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    final void parseTypeReference() throws IOException, ParserException {
        
        if (lexer.lexSkipWS(JavaToken.IDENTIFIER) != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        final int identifierContext = writeCurContext();
        final long identifier = getStringRef();
        
        final ReferenceType referenceType = ReferenceType.REFERENCE;
        
        if (lexer.lexSkipWS(JavaToken.PERIOD) == JavaToken.PERIOD) {
            parseNameListUntilOtherToken(identifierContext, identifier, names -> {
    
                // call listener with the.namespace.SomeClass from names
                final int startContext = listenerHelper.callScopedTypeReferenceListenersStartAndPart(names, referenceType, null);
                
                // add any generics
                tryParseGenericTypeArguments();
                
                listenerHelper.callScopedTypeReferenceListenersEnd(startContext, getLexerContext());
            });
        }
        else {
            listenerHelper.callNonScopedTypeReferenceListenersStart(identifierContext, identifier, null, referenceType);

            tryParseGenericTypeArguments();
            
            listenerHelper.callNonScopedTypeReferenceListenersEnd(identifierContext, getLexerContext(), referenceType);
        }
    }
}
