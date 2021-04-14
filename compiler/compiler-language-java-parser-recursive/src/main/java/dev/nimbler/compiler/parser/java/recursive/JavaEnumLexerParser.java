package dev.nimbler.compiler.parser.java.recursive;

import java.io.IOException;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.parser.recursive.cached.expressions.ExpressionCacheList;
import dev.nimbler.compiler.parser.recursive.cached.expressions.ParametersList;

abstract class JavaEnumLexerParser<COMPILATION_UNIT> extends JavaClassLexerParser<COMPILATION_UNIT> {

    JavaEnumLexerParser(String file, Lexer<JavaToken, CharInput> lexer, Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        super(file, lexer, tokenizer, listener);
    }

    final void parseEnum(
            int enumStartContext,
            long enumKeyword,
            int enumKeywordContext) throws IOException, ParserException {
        
        final JavaToken classNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
        if (classNameToken != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        final long className = getStringRef();
        final int classNameContext = writeCurContext();

        // Initial context of class is either class visibility or subclassing or class keyword
        listener.onEnumStart(enumStartContext, enumKeyword, enumKeywordContext, className, classNameContext);
        
        // Implements?
        if (lexer.lexSkipWS(JavaToken.IMPLEMENTS) == JavaToken.IMPLEMENTS) {
            parseImplements();
        }
        
        if (lexer.lexSkipWS(JavaToken.LBRACE) != JavaToken.LBRACE) {
            throw lexer.unexpectedToken();
        }

        parseEnumValues();
        
        parseClassBody(className);
        
        listener.onEnumEnd(enumStartContext, getLexerContext());
    }
    
    private static final JavaToken [] INITIAL_ENUM_VALUE_TOKENS = new JavaToken [] {
      
            JavaToken.IDENTIFIER,
            
            JavaToken.SEMI
    };
    
    private void parseEnumValues() throws IOException, ParserException {

        final JavaToken valueToken = lexer.lexSkipWS(INITIAL_ENUM_VALUE_TOKENS);
        
        if (valueToken == JavaToken.SEMI) {

            // No values
        }
        else if (valueToken == JavaToken.IDENTIFIER) {
            
            final int constantNameContext = writeCurContext();
            final long constantName = getStringRef();
            
            parseEnumValue(constantNameContext, constantName);

            parseRemainingEnumValues();
        }
        else {
            throw lexer.unexpectedToken();
        }
    }

    private static final JavaToken [] ENUM_VALUE_TOKENS = new JavaToken [] {
            
            JavaToken.IDENTIFIER
    };
    
    private static final JavaToken [] AFTER_ENUM_VALUE_TOKENS = new JavaToken [] {
            
            JavaToken.COMMA,
            JavaToken.SEMI
    };

    private void parseRemainingEnumValues() throws IOException, ParserException {

        boolean done = false;

        do {
            final JavaToken afterValueToken = lexer.lexSkipWS(AFTER_ENUM_VALUE_TOKENS);
            
            switch (afterValueToken) {
            
            case COMMA:
                final JavaToken valueToken = lexer.lexSkipWS(ENUM_VALUE_TOKENS);
                
                switch (valueToken) {
                case IDENTIFIER:
                    parseEnumValue(writeCurContext(), getStringRef());
                    break;
                    
                default:
                    throw lexer.unexpectedToken();
                }
                break;
                
            case SEMI:
                done = true;
                break;
                
            default:
                throw lexer.unexpectedToken();
            }
            
        } while (!done);
    }

    private void parseEnumValue(int enumConstantStartContext, long stringRef) throws IOException, ParserException {

        listener.onEnumConstantStart(enumConstantStartContext, stringRef);
        
        if (lexer.lexSkipWS(JavaToken.LPAREN) == JavaToken.LPAREN) {
            // enum constructor
            parseEnumConstantParameters(stringRef, enumConstantStartContext);
        }

        listener.onEnumConstantEnd(enumConstantStartContext, getLexerContext());
    }
    
    private void parseEnumConstantParameters(long name, int nameContext) throws IOException, ParserException {
        
        if (!baseClassExpressionCache.isEmpty()) {
            throw new IllegalStateException();
        }

        baseClassExpressionCache.addName(nameContext, name);
        baseClassExpressionCache.addMethodInvocationStart(writeCurContext());
        
        parseMethodInvocationParametersToCache();
        
        baseClassExpressionCache.addMethodInvocationEnd();
        
        final ExpressionCacheList methodinvocationList = baseClassExpressionCache.getStartingPoint();

        if (methodinvocationList.count() != 1) {
            throw new IllegalStateException();
        }
        
        final ParametersList parameters = methodinvocationList.getParametersAt(0);

        baseClassExpressionCache.getApplier().applyParameters(parameters, listener);
        
        baseClassExpressionCache.clear();
    }
}
