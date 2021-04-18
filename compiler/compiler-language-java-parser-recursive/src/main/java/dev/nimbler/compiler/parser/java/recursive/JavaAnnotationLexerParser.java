package dev.nimbler.compiler.parser.java.recursive;

import java.io.IOException;

import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.Tokenizer;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;

import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.parser.recursive.cached.annotations.CachedAnnotation;
import dev.nimbler.compiler.parser.recursive.cached.annotations.CachedAnnotationsList;
import dev.nimbler.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElement;
import dev.nimbler.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElementsList;
import dev.nimbler.compiler.parser.recursive.cached.names.NamesList;
import dev.nimbler.compiler.util.ContextRef;

abstract class JavaAnnotationLexerParser<COMPILATION_UNIT> extends JavaExpressionLexerParser<COMPILATION_UNIT> {

    JavaAnnotationLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }
    
    static <COMPILATION_UNIT> void apply(CachedAnnotationsList annotationsList, IterativeParseTreeListener<COMPILATION_UNIT> listener) throws IOException, ParserException {
        
        annotationsList.complete(annotations -> {
            
            final int num = annotations.count();
            
            for (int i = 0; i < num; ++ i) {

                final CachedAnnotation annotation = annotations.getAnnotation(i);
                
                annotation.getTypeName().complete(typeName -> listener.onAnnotationStart(annotation.getStartContext(), typeName));

                final CachedAnnotationElementsList elementsList = annotation.getElements();
                
                if (elementsList != null) {
                    apply(elementsList, listener);
                }
                
                listener.onAnnotationEnd(annotation.getStartContext(), annotation.getEndContext());
            }
        });
    }

    private static <COMPILATION_UNIT> void apply(
            CachedAnnotationElementsList annotationElementsList,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) throws IOException, ParserException {
        
        annotationElementsList.complete(elements -> {

            final int num = elements.count();
            
            for (int i = 0; i < num; ++ i) {

                final CachedAnnotationElement element = elements.getAnnotationElement(i);
                
                listener.onAnnotationElementStart(element.getStartContext(), element.getName(), element.getNameContext());
                switch (element.getType()) {
                
                case NAME:
                    break;
                    
                case ANNOTATION:
                    apply(element.getAnnotations(), listener);
                    break;
                    
                case EXPRESSION:
                    applyAndClearExpressionCache(element.getExpressionCache(), listener);
                    break;
                    
                case VALUE_LIST:
                    apply(element.getValueList(), listener);
                    break;

                default:
                    throw new UnsupportedOperationException();
                }

                listener.onAnnotationElementEnd(element.getStartContext(), element.getEndContext());
            }
        });
    }

    final void parseAnnotation(int startContext) throws IOException, ParserException {
        
        final CachedAnnotationsList list = startScratchAnnotations();
        
        parseAnnotation(list, startContext);
        
        apply(list, listener);
    }

    final void parseAnnotation(CachedAnnotationsList list, int startContext) throws IOException, ParserException {
        
        final NamesList namesList = startScratchNameParts();
        
        parseNames(namesList);

        final CachedAnnotationElementsList annotationElements = parseAnyAnnotationElements();
        
        list.addAnnotation(startContext, namesList, annotationElements, getLexerContext());
    }
    
    private CachedAnnotationElementsList parseAnyAnnotationElements() throws IOException, ParserException {

        final CachedAnnotationElementsList list;
        
        if (lexer.lexSkipWS(JavaToken.LPAREN) == JavaToken.LPAREN) {
            
            // Is this @Annotation(identifier = value) or @Annotation(STATIC_CONSTANT)
            // or @Annotation(LITERAL)
            
            if (lexer.lexSkipWS(JavaToken.IDENTIFIER) == JavaToken.IDENTIFIER) {
         
                final int identifierContext = writeCurContext();
                final int elementStartContext = writeContext(identifierContext);
                        
                // @Annotation(identifier = value) or @Annotation(STATIC_CONSTANT)

                final long stringRef = getStringRef();

                if (lexer.lexSkipWS(JavaToken.ASSIGN) == JavaToken.ASSIGN) {

                    list = startScratchAnnotationElements();
                    
                    // @Annotation(identifier = value)
                    parseExpressionOrAnnotationOrList(list, elementStartContext, stringRef, identifierContext);
                    
                    parsePerhapsMultipleElementValues(list);
                }
                else {
                    list = startScratchAnnotationElements();
                    
                    final CachedAnnotationElement element = list.addExpression(
                            elementStartContext,
                            StringRef.STRING_NONE,
                            ContextRef.NONE,
                            contextWriter,
                            languageOperatorPrecedence);
                    
                    element.getExpressionCache().addName(identifierContext, stringRef);
                    
                }
            }
            else {
                list = startScratchAnnotationElements();

                parseExpressionOrAnnotationOrList(list, writeCurContext(), StringRef.STRING_NONE, ContextRef.NONE);
            }
            
            if (lexer.lexSkipWS(JavaToken.RPAREN) != JavaToken.RPAREN) {
                throw lexer.unexpectedToken();
            }
        }
        else {
            list = null;
        }
        
        return list;
    }
    
    private void parsePerhapsMultipleElementValues(CachedAnnotationElementsList elementsList) throws IOException, ParserException {

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

            parseExpressionOrAnnotationOrList(elementsList, otherElementStartContext, otherIdentifierRef, otherIdentifierContext);
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
            CachedAnnotationElementsList list,
            int startContext,
            long identifier,
            int identifierContext) throws IOException, ParserException {

        final JavaToken token = lexer.lexSkipWS(EXPRESSION_OR_ANNOTATION_OR_LIST_TOKENS);
        
        if (token == JavaToken.AT) {
            
            // @TheAnnotation(@OtherAnnotation)

            final CachedAnnotationsList annotationsList = startScratchAnnotations();
                    
            list.addAnnotation(writeCurContext(), identifier, identifierContext, annotationsList);

            parseAnnotation(annotationsList, writeCurContext());
        }
        else if (token == JavaToken.LBRACE) {

            final int listStartContext = writeCurContext();

            final CachedAnnotationElementsList subElementsList = startScratchAnnotationElements();
            
            list.addValueList(listStartContext, identifier, identifierContext, subElementsList);
            
            for (;;) {
                
                if (lexer.lexSkipWS(JavaToken.AT) == JavaToken.AT) {
                    
                    final CachedAnnotationsList annotationsList = startScratchAnnotations();
                            
                    subElementsList.addAnnotation(listStartContext, StringRef.STRING_NONE, ContextRef.NONE, annotationsList);
                    
                    parseAnnotation(annotationsList, startContext);
                }
                else {
                    final CachedAnnotationElement element = subElementsList.addExpression(
                            listStartContext,
                            StringRef.STRING_NONE,
                            ContextRef.NONE,
                            contextWriter,
                            languageOperatorPrecedence);

                    parseExpressionToCache(element.getExpressionCache());
                    
                    element.initEndContext(getLexerContext());
                }
                
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
            final CachedAnnotationElement element = list.addExpression(
                    startContext,
                    identifier,
                    identifierContext,
                    contextWriter,
                    languageOperatorPrecedence);

            parseExpressionToCache(element.getExpressionCache());
            
            element.initEndContext(getLexerContext());
        }
    }
}
