package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaEnumLexerParser<COMPILATION_UNIT> extends JavaClassLexerParser<COMPILATION_UNIT> {

    JavaEnumLexerParser(String file, Lexer<JavaToken, CharInput> lexer, Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
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
        
        if (lexer.lexSkipWS(JavaToken.LBRACE) != JavaToken.LBRACE) {
            throw lexer.unexpectedToken();
        }

        parseEnumValues();
        
        if (lexer.lexSkipWS(JavaToken.RBRACE) != JavaToken.RBRACE) {
            throw lexer.unexpectedToken();
        }
        
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
            
            parseEnumValue(writeCurContext(), getStringRef());

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

    private void parseEnumValue(int enumConstantStartContext, long stringRef) {

        listener.onEnumConstantStart(enumConstantStartContext, stringRef);
        
        listener.onEnumConstantEnd(enumConstantStartContext, getLexerContext());
    }
}
