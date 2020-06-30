package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaAnnotationLexerParser<COMPILATION_UNIT> extends JavaExpressionLexerParser<COMPILATION_UNIT> {

    JavaAnnotationLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    final void parseAnnotation(int startContext) throws IOException, ParserException {
        
        parseNameListUntilOtherToken(names -> {

            listener.onAnnotationStart(startContext, names);
        
            parseAnyAnnotationElements();
            
            listener.onAnnotationEnd(startContext, getLexerContext());
        });
    }
    
    private void parseAnyAnnotationElements() throws IOException, ParserException {

        if (lexer.lexSkipWS(JavaToken.LPAREN) == JavaToken.LPAREN) {
            
            // Is this @Annotation(identifier = value) or @Annotation(STATIC_CONSTANT)
            // or @Annotation(LITERAL)
            
            if (lexer.lexSkipWS(JavaToken.IDENTIFIER) == JavaToken.IDENTIFIER) {
         
                final int identifierContext = writeCurContext();
                final int elementStartContext = writeContext(identifierContext);
                        
                // @Annotation(identifier = value) or @Annotation(STATIC_CONSTANT)

                final long stringRef = getStringRef();

                if (lexer.lexSkipWS(JavaToken.ASSIGN) == JavaToken.ASSIGN) {

                    // @Annotation(identifier = value)
                    parseExpressionOrAnnotationOrList(elementStartContext, stringRef, identifierContext);
                    
                    parsePerhapsMultipleElementValues();
                }
                else {
                    listener.onAnnotationElementStart(elementStartContext, StringRef.STRING_NONE, ContextRef.NONE);
                    
                    listener.onVariableReference(identifierContext, stringRef);

                    listener.onAnnotationElementEnd(elementStartContext, getLexerContext());
                }
            }
            else {
                parseExpressionOrAnnotationOrList(writeCurContext(), StringRef.STRING_NONE, ContextRef.NONE);
            }
            
            if (lexer.lexSkipWS(JavaToken.RPAREN) != JavaToken.RPAREN) {
                throw lexer.unexpectedToken();
            }
        }
    }
    
    private void parsePerhapsMultipleElementValues() throws IOException, ParserException {

        // List of more element values?
        for (;;) {
            
            if (lexer.lexSkipWS(JavaToken.COMMA) != JavaToken.COMMA) {
                break;
            }
            
            final JavaToken otherIdentifier = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (otherIdentifier != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            final int otherIdentifierContext = writeCurContext();
            final int otherElementStartContext = writeContext(otherIdentifierContext);
            
            final long otherIdentifierRef = getStringRef();
            
            if (lexer.lexSkipWS(JavaToken.ASSIGN) != JavaToken.ASSIGN) {
                throw lexer.unexpectedToken();
            }

            parseExpressionOrAnnotationOrList(otherElementStartContext, otherIdentifierRef, otherIdentifierContext);
        }
    }

    private static final JavaToken [] EXPRESSION_OR_ANNOTATION_OR_LIST_TOKENS = new JavaToken [] {
            JavaToken.AT,
            JavaToken.LBRACE
    };
    
    private static final JavaToken [] AFTER_ANNOTATION_ELEMENT_LIST_TOKENS = new JavaToken [] {
            JavaToken.COMMA,
            JavaToken.RBRACE
    };

    private void parseExpressionOrAnnotationOrList(
            int startContext,
            long identifier,
            int identifierContext) throws IOException, ParserException {

        final JavaToken token = lexer.lexSkipWS(EXPRESSION_OR_ANNOTATION_OR_LIST_TOKENS);
        
        listener.onAnnotationElementStart(startContext, identifier, identifierContext);

        if (token == JavaToken.AT) {
            
            // @TheAnnotation(@OtherAnnotation)
            
            parseAnnotation(writeCurContext());
        }
        else if (token == JavaToken.LBRACE) {
            
            for (;;) {

                final int listStartContext = writeCurContext();
                
                listener.onAnnotationElementStart(listStartContext, StringRef.STRING_NONE, ContextRef.NONE);
                
                if (lexer.lexSkipWS(JavaToken.AT) == JavaToken.AT) {
                    parseAnnotation(startContext);
                }
                else {
                    parseExpression();
                }
                
                listener.onAnnotationElementEnd(listStartContext, getLexerContext());
                
                final JavaToken listToken = lexer.lexSkipWS(AFTER_ANNOTATION_ELEMENT_LIST_TOKENS);
                
                if (listToken == JavaToken.COMMA) {
                    // Continue to iterate
                }
                else if (listToken == JavaToken.RBRACE) {
                    break;
                }
                else {
                    throw lexer.unexpectedToken();
                }
            }
        }
        else {
            parseExpression();
        }
        
        listener.onAnnotationElementEnd(startContext, getLexerContext());
    }

}
