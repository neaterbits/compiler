package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaTypeParametersLexerParser<COMPILATION_UNIT> extends JavaTypeArgumentsLexerParser<COMPILATION_UNIT> {

    JavaTypeParametersLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    final void parseGenericTypeParameters() throws IOException, ParserException {
        
        final int startContext = writeCurContext();
        
        listener.onGenericTypeParametersStart(startContext);
        
        for (;;) {
            
            if (lexer.lexSkipWS(JavaToken.IDENTIFIER) == JavaToken.IDENTIFIER) {
    
                final int nameContext = writeCurContext();
                final int namedTypeStartContext = writeContext(nameContext);
                
                listener.onNamedGenericTypeParameterStart(namedTypeStartContext, getStringRef(), nameContext);
                
                if (lexer.lexSkipWS(JavaToken.EXTENDS) == JavaToken.EXTENDS) {
                    parseTypeBound(writeCurContext(), TypeBoundType.EXTENDS);
                }
                
                listener.onNamedGenericTypeParameterEnd(namedTypeStartContext, getLexerContext());
            }
            else {
                throw lexer.unexpectedToken();
            }
            
            final JavaToken afterTypeArgument = lexer.lexSkipWS(AFTER_TYPE_ARGUMENT_TOKENS);
            
            if (afterTypeArgument == JavaToken.COMMA) {
                // Continue on next type
            }
            else if (afterTypeArgument == JavaToken.GT) {
                break;
            }
            else {
                throw lexer.unexpectedToken();
            }
        }
        
        listener.onGenericTypeParametersEnd(startContext, getLexerContext());
    }
    
    private void parseTypeBound(int startContext, TypeBoundType type) throws IOException, ParserException {

        listener.onTypeBoundStart(startContext, type);

        parseTypeReference();

        listener.onTypeBoundEnd(startContext, getLexerContext());
    }
}
