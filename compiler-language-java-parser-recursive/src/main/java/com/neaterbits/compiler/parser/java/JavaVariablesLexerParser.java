package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

public abstract class JavaVariablesLexerParser<COMPILATION_UNIT>
    extends JavaTypesLexerParser<COMPILATION_UNIT> {

    protected JavaVariablesLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    
    final void parseUserType(int initialPartContext) throws ParserException, IOException {
        
        final long stringRef = getStringRef();

        JavaToken scopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
        
        parseUserType(initialPartContext, stringRef, scopeToken == JavaToken.PERIOD);
    }

    private void parseUserType(int initialPartContext, long stringRef, boolean gotPeriodToken) throws ParserException, IOException {
        
        if (gotPeriodToken) {
            parseUserTypeAfterPeriod(initialPartContext, stringRef);
        }
        else {
            final int startContext = writeContext(initialPartContext);

            listener.onNonScopedTypeReferenceStart(startContext, stringRef, ReferenceType.REFERENCE);
            
            listener.onNonScopedTypeReferenceEnd(startContext, getLexerContext());
        }
    }

    private void parseUserTypeAfterPeriod(int initialPartContext, long stringRef) throws IOException, ParserException {
        
        final int typeStartContext = writeContext(initialPartContext);

        listener.onScopedTypeReferenceStart(typeStartContext, ReferenceType.REFERENCE);
        
        final int namesStartContext = writeContext(typeStartContext);
        
        listener.onScopedTypeReferenceNameStart(namesStartContext);
        
        listener.onScopedTypeReferenceNamePart(initialPartContext, stringRef);

        for (;;) {

            final JavaToken partToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (partToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
                
            listener.onScopedTypeReferenceNamePart(writeCurContext(), getStringRef());
            
            final JavaToken endOfScopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
            
            if (endOfScopeToken != JavaToken.PERIOD) {
                break;
            }
        }
        
        listener.onScopedTypeReferenceNameEnd(namesStartContext, getLexerContext());

        listener.onScopedTypeReferenceEnd(typeStartContext, getLexerContext());
    }

    private static final JavaToken [] AFTER_VARIABLE_NAME = new JavaToken [] {
            
            JavaToken.SEMI,
            JavaToken.COMMA,
            JavaToken.ASSIGN,
            JavaToken.LBRACKET
    };
    
    private static final JavaToken [] AFTER_VARIABLE_INITIALIZER = new JavaToken [] {
            JavaToken.SEMI,
            JavaToken.COMMA
    };

    final void parseVariableDeclaratorList() throws IOException, ParserException {
        
        parseVariableDeclaratorList(StringRef.STRING_NONE, ContextRef.NONE);
    }

    final void parseVariableDeclaratorList(long initialVarName, int initialVarNameContext) throws IOException, ParserException {

        boolean done = false;
        
        boolean initialIteration = true;
        
        do {
            final long identifier;
            final int declaratorStartContext;
            
            if (initialIteration && initialVarName != StringRef.STRING_NONE) {
                identifier = initialVarName;
                declaratorStartContext = initialVarNameContext;
            }
            else {
                final JavaToken fieldNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
                
                if (fieldNameToken != JavaToken.IDENTIFIER) {
                    throw lexer.unexpectedToken();
                }

                identifier = getStringRef();
                declaratorStartContext = writeCurContext();
            }
            
            initialIteration = false;

            final int declaratorNameContext = writeContext(declaratorStartContext);
            
            final JavaToken afterVarNameToken = lexer.lexSkipWS(AFTER_VARIABLE_NAME);
            
            switch (afterVarNameToken) {
            case COMMA:
            case SEMI:
                listener.onVariableDeclaratorStart(declaratorStartContext);
                
                listener.onVariableName(declaratorStartContext, identifier, declaratorNameContext);
                
                listener.onVariableDeclaratorEnd(declaratorStartContext, getLexerContext());
                
                if (afterVarNameToken == JavaToken.SEMI) {
                    done = true;
                }
                break;

            case ASSIGN:
                listener.onVariableDeclaratorStart(declaratorStartContext);
                
                listener.onVariableName(declaratorStartContext, identifier, declaratorNameContext);
                
                parseExpressionList();

                listener.onVariableDeclaratorEnd(declaratorStartContext, getLexerContext());
                
                // Skip out if next is semicolon
                switch (lexer.lexSkipWS(AFTER_VARIABLE_INITIALIZER)) {
                case COMMA:
                    break;
                    
                case SEMI:
                    done = true;
                    break;
                    
                default:
                    throw lexer.unexpectedToken();
                }
                break;
                
            default:
                throw lexer.unexpectedToken();
            }
        } while (!done);
    }
}
