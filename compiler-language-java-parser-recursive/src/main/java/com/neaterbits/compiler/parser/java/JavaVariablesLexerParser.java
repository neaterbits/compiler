package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.Context;
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
            
            tryParseGenericTypeParameters();
            
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
        
        boolean done = false;
        
        do {
            final JavaToken fieldNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (fieldNameToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }

            final long identifier = getStringRef();
            final int identifierContext = writeCurContext();
            
            final Context variableDeclaratorEndContext = initScratchContext();
            
            final JavaToken afterVarNameToken = lexer.lexSkipWS(AFTER_VARIABLE_NAME);
            
            switch (afterVarNameToken) {
            case COMMA:
            case SEMI:
                final int variableDeclaratorStartContext = writeContext(identifierContext);

                listenerHelper.onVariableDeclarator(
                        variableDeclaratorStartContext,
                        identifier,
                        identifierContext,
                        variableDeclaratorEndContext);
                
                if (afterVarNameToken == JavaToken.SEMI) {
                    done = true;
                }
                break;

            case ASSIGN:
                done = parseVariableInitializer(identifier, identifierContext);
                break;
                
            default:
                throw lexer.unexpectedToken();
            }
        } while (!done);
    }

    final void parseVariableDeclaratorList(
            long initialVarName,
            int initialVarNameContext,
            Context initialVariableDeclaratorEndContext) throws IOException, ParserException {

        if (initialVarName == StringRef.STRING_NONE) {
            throw new IllegalArgumentException();
        }
        
        if (initialVarNameContext == ContextRef.NONE) {
            throw new IllegalArgumentException();
        }
        
        boolean done = false;
        
        long identifier = initialVarName;
        int identifierContext = initialVarNameContext;
        Context variableDeclaratorEndContext = initialVariableDeclaratorEndContext;

        do {
            final JavaToken afterVarNameToken = lexer.lexSkipWS(AFTER_VARIABLE_NAME);
            
            switch (afterVarNameToken) {
            case COMMA:
            case SEMI:
                final int variableDeclaratorStartContext = writeContext(identifierContext);

                listenerHelper.onVariableDeclarator(
                        variableDeclaratorStartContext,
                        identifier,
                        identifierContext,
                        variableDeclaratorEndContext);
                
                if (afterVarNameToken == JavaToken.SEMI) {
                    done = true;
                }
                break;

            case ASSIGN:
                done = parseVariableInitializer(identifier, identifierContext);
                break;
                
            default:
                throw lexer.unexpectedToken();
            }
            
            if (!done) {
                final JavaToken fieldNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
                
                if (fieldNameToken != JavaToken.IDENTIFIER) {
                    throw lexer.unexpectedToken();
                }

                identifier = getStringRef();
                identifierContext = writeCurContext();
                variableDeclaratorEndContext = initScratchContext();
            }
        } while (!done);
    }
    
    private boolean parseVariableInitializer(long identifier, int identifierContext) throws IOException, ParserException {
        
        final boolean done;
        
        final int variableDeclaratorStartContext = writeContext(identifierContext);
        
        listenerHelper.onVariableDeclarator(
                variableDeclaratorStartContext,
                identifier,
                identifierContext,
                this::parseExpressionList,
                this::getLexerContext);
        
        // Skip out if next is semicolon
        switch (lexer.lexSkipWS(AFTER_VARIABLE_INITIALIZER)) {
        case COMMA:
            done = false;
            break;
            
        case SEMI:
            done = true;
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
        
        return done;
    }
}
