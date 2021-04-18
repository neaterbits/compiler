package dev.nimbler.compiler.parser.java.recursive;

import java.io.IOException;

import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.Tokenizer;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;

import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.types.ReferenceType;

abstract class JavaTypesLexerParser<COMPILATION_UNIT> extends JavaAnnotationLexerParser<COMPILATION_UNIT> {

    abstract void tryParseGenericTypeArguments() throws IOException, ParserException;
    
    static final JavaToken [] SCALAR_TYPE_TOKENS = new JavaToken [] {
            
            JavaToken.VOID,
            
            JavaToken.BYTE,
            JavaToken.SHORT,
            JavaToken.INT,
            JavaToken.LONG,
            JavaToken.FLOAT,
            JavaToken.DOUBLE,
            JavaToken.CHAR
    };
    
    JavaTypesLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }
    
    final void parseScalarOrTypeReference() throws IOException, ParserException {
        
        final JavaToken scalarToken = lexer.lexSkipWS(SCALAR_TYPE_TOKENS);
        
        if (scalarToken != JavaToken.NONE) {
            listener.onLeafTypeReference(writeCurContext(), getStringRef(), ReferenceType.SCALAR);
        }
        else {
            parseTypeReference();
        }
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
