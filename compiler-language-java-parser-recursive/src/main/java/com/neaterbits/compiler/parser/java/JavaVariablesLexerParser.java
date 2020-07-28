package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

public abstract class JavaVariablesLexerParser<COMPILATION_UNIT>
    extends JavaTypeParametersLexerParser<COMPILATION_UNIT> {
        
    protected JavaVariablesLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    private static final JavaToken [] AFTER_VARIABLE_NAME = new JavaToken [] {
            
            JavaToken.SEMI,
            JavaToken.COMMA,
            JavaToken.ASSIGN,
    };

    private static final JavaToken [] AFTER_VARIABLE_INITIALIZER = new JavaToken [] {
            JavaToken.SEMI,
            JavaToken.COMMA
    };
    
    final void parseVariableDeclaratorList() throws IOException, ParserException {
        
        // Parse any initial array indicators after type
        final int numDims = parseArrayIndicators(0);

        if (lexer.lexSkipWS(JavaToken.IDENTIFIER) != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }

        boolean done = parseAfterVariable(getStringRef(), writeCurContext(), numDims);

        while (!done) {

            if (lexer.lexSkipWS(JavaToken.IDENTIFIER) != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            done = parseAfterVariable(getStringRef(), writeCurContext());
        }
    }
    
    final int parseArrayIndicators(int num) throws IOException, ParserException {
     
        int numDims = num;
        
        for (;;) {
            
            if (lexer.lexSkipWS(JavaToken.LBRACKET) != JavaToken.LBRACKET) {
                break;
            }

            if (lexer.lexSkipWS(JavaToken.RBRACKET) != JavaToken.RBRACKET) {
                throw lexer.unexpectedToken();
            }

            ++ numDims;
        }
        
        return numDims;
    }
    
    private boolean parseAfterVariable(long identifier, int identifierContext) throws IOException, ParserException {
        
        return parseAfterVariable(identifier, identifierContext, 0);
    }

    private boolean parseAfterVariable(long identifier, int identifierContext, int numDims) throws IOException, ParserException {

        final boolean done;
        
        final Context variableDeclaratorEndContext = initScratchContext();

        numDims = parseArrayIndicators(numDims);

        final JavaToken afterVarNameToken = lexer.lexSkipWS(AFTER_VARIABLE_NAME);

        switch (afterVarNameToken) {
        
        case COMMA:
        case SEMI:
            final int variableDeclaratorStartContext = writeContext(identifierContext);

            listenerHelper.onVariableDeclarator(
                    variableDeclaratorStartContext,
                    identifier,
                    identifierContext,
                    numDims,
                    variableDeclaratorEndContext);
            
            done = afterVarNameToken == JavaToken.SEMI;
            break;

        case ASSIGN:
            done = parseVariableInitializer(identifier, identifierContext);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
        
        return done;
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
                        0,
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
    
    final boolean parseVariableInitializer(long identifier, int identifierContext) throws IOException, ParserException {
        
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
