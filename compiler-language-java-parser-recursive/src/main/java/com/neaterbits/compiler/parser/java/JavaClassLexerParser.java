package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaClassLexerParser<COMPILATION_UNIT> extends JavaMemberLexerParser<COMPILATION_UNIT> {

    JavaClassLexerParser(String file, Lexer<JavaToken, CharInput> lexer, Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        super(file, lexer, tokenizer, listener);
    }

    final void parseClass(
            int classStartContext,
            long classKeyword,
            int classKeywordContext) throws IOException, ParserException {
        
        final JavaToken classNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
        if (classNameToken != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        final long className = getStringRef();
        final int classNameContext = writeCurContext();

        // Initial context of class is either class visibility or subclassing or class keyword
        listener.onClassStart(classStartContext, classKeyword, classKeywordContext, className, classNameContext);
        
        parseClassGenericsOrExtendsOrImplementsOrBody(className);
        
        listener.onClassEnd(classStartContext, getLexerContext());
    }
    
    private static final JavaToken [] AFTER_CLASSNAME = new JavaToken [] {
            JavaToken.LT, // generics type start
            JavaToken.EXTENDS,
            JavaToken.IMPLEMENTS,
            JavaToken.LBRACE
    };

    private void parseClassGenericsOrExtendsOrImplementsOrBody(long implementingClassName) throws IOException, ParserException {
        
        final JavaToken afterClassName = lexer.lexSkipWS(AFTER_CLASSNAME);
        
        switch (afterClassName) {
        
        case LT:
            parseGenericTypeArguments();
            
            parseExtendsOrImplementsOrBody(implementingClassName);
            break;
        
        case EXTENDS:
            parseExtends();
            
            parseImplementsOrBody(implementingClassName);
            break;
            
        case IMPLEMENTS:
            parseImplements();
         
            parseClassBodyWithLBrace(implementingClassName);
            break;
            
        case LBRACE:
            parseClassBody(implementingClassName);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }

    private static final JavaToken [] EXTENDS_OR_IMPLEMENTS_OR_BODY = new JavaToken [] {
            JavaToken.EXTENDS,
            JavaToken.IMPLEMENTS,
            JavaToken.LBRACE
    };

    private void parseExtendsOrImplementsOrBody(long implementingClassName) throws IOException, ParserException {
        
        final JavaToken afterClassName = lexer.lexSkipWS(EXTENDS_OR_IMPLEMENTS_OR_BODY);
        
        switch (afterClassName) {
        
        case EXTENDS:
            parseExtends();
            
            parseImplementsOrBody(implementingClassName);
            break;
            
        case IMPLEMENTS:
            parseImplements();
            
            parseClassBody(implementingClassName);
            break;
            
        case LBRACE:
            parseClassBody(implementingClassName);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }

    private static final JavaToken [] AFTER_EXTENDS = new JavaToken [] {
            JavaToken.IMPLEMENTS,
            JavaToken.LBRACE
    };

    private void parseImplementsOrBody(long implementingClassName) throws IOException, ParserException {
        
        final JavaToken afterExtends = lexer.lexSkipWS(AFTER_EXTENDS);
        
        switch (afterExtends) {
        
        case IMPLEMENTS:
            parseImplements();
            
            parseClassBodyWithLBrace(implementingClassName);
            break;
            
        case LBRACE:
            parseClassBody(implementingClassName);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private void parseExtends() throws IOException, ParserException {

        final int extendsStartContext = writeCurContext();
        
        final long extendsKeyword = lexer.getStringRef();
        final int extendsKeywordContext = writeCurContext();
        
        listener.onClassExtendsStart(
                extendsStartContext,
                extendsKeyword,
                extendsKeywordContext);
        
        parseScopedName(listener::onClassExtendsNamePart);
        
        listener.onClassExtendsEnd(extendsStartContext, getLexerContext());
    }
    
    final void parseImplements() throws IOException, ParserException {
        
        final int implementsStartContext = writeCurContext();
        final int implementsKeywordContext = writeContext(implementsStartContext);
        
        final long implementsKeyword = lexer.getStringRef();
        
        listener.onImplementsStart(implementsStartContext, implementsKeyword, implementsKeywordContext);
    
        for (;;) {

            final int implementsTypeStartContext = writeCurContext();
            
            listener.onImplementsTypeStart(implementsTypeStartContext);

            parseScopedName(listener::onImplementsNamePart);

            listener.onImplementsTypeEnd(implementsTypeStartContext, getLexerContext());

            if (lexer.lexSkipWS(JavaToken.COMMA) != JavaToken.COMMA) {
                break;
            }
        }
        
        listener.onImplementsEnd(implementsStartContext, getLexerContext());
    }

    private void parseClassBodyWithLBrace(long implementingClassName) throws IOException, ParserException {
        
        if (lexer.lexSkipWS(JavaToken.LBRACE) != JavaToken.LBRACE) {
            throw lexer.unexpectedToken();
        }

        parseClassBody(implementingClassName);
    }

    final void parseClassBody(long implementingClassName) throws IOException, ParserException {
        
        boolean memberFound;
        
        do {
            memberFound = parseMember(implementingClassName);
        } while (memberFound);
        
        if (lexer.lexSkipWS(JavaToken.RBRACE) != JavaToken.RBRACE) {
            throw lexer.unexpectedToken();
        }
    }
}
