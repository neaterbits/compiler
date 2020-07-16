package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgumentsList;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaTypesLexerParser<COMPILATION_UNIT> extends JavaAnnotationLexerParser<COMPILATION_UNIT> {

    JavaTypesLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    private static final JavaToken [] GENERIC_TYPE_TOKENS = new JavaToken [] {
            JavaToken.IDENTIFIER,
            JavaToken.QUESTION_MARK
    };
    
    private static final JavaToken [] AFTER_TYPE_ARGUMENT_TOKENS = new JavaToken [] {
            JavaToken.COMMA,
            JavaToken.GT
    };
    
    final void parseGenericTypeArguments() throws IOException, ParserException {
        
        final int startContext = writeCurContext();
        
        listener.onGenericClassDefinitionTypeListStart(startContext);
        
        for (;;) {
            
            final JavaToken typeArgumentToken = lexer.lexSkipWS(GENERIC_TYPE_TOKENS);
            
            if (typeArgumentToken == JavaToken.IDENTIFIER) {
    
                final int nameContext = writeCurContext();
                final int namedTypeStartContext = writeContext(nameContext);
                
                listener.onGenericNamedTypeStart(namedTypeStartContext, getStringRef(), nameContext);
                
                if (lexer.lexSkipWS(JavaToken.EXTENDS) == JavaToken.EXTENDS) {
                    parseTypeBound(writeCurContext(), TypeBoundType.EXTENDS);
                }
                
                listener.onGenericNamedTypeEnd(namedTypeStartContext, getLexerContext());
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
        
        listener.onGenericClassDefinitionTypeListEnd(startContext, getLexerContext());
    }
    
    private void parseTypeBound(int startContext, TypeBoundType type) throws IOException, ParserException {

        listener.onTypeBoundStart(startContext, type);

        parseTypeReference();

        listener.onTypeBoundEnd(startContext, getLexerContext());
    }
    
    private void parseTypeReference() throws IOException, ParserException {
        
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
                tryParseGenericTypeParameters();
                
                listenerHelper.callScopedTypeReferenceListenersEnd(startContext, getLexerContext());
            });
        }
        else {
            listenerHelper.callNonScopedTypeReferenceListenersStart(identifierContext, identifier, null, referenceType);

            tryParseGenericTypeParameters();
            
            listenerHelper.callNonScopedTypeReferenceListenersEnd(identifierContext, getLexerContext(), referenceType);
        }
    }

    final TypeArgumentsList tryParseGenericTypeParametersToScratchList() throws IOException, ParserException {
        
        final TypeArgumentsList typeArgumentsList;
        
        if (lexer.lexSkipWS(JavaToken.LT) == JavaToken.LT) {

            typeArgumentsList = startScratchTypeArguments();

            final int startContext = writeCurContext();
            
            final ParseFunction parseTypeReference = () -> {
                
                // Parse the type to names list
                final NamesList namesList = startScratchNameParts();
                
                parseNames(namesList);

                final TypeArgumentsList genericTypes = tryParseGenericTypeParametersToScratchList();
                
                typeArgumentsList.addConcreteType(namesList, genericTypes, getLexerContext());
            };
            
            parseGenericTypeParameters(startContext, parseTypeReference);
            
            typeArgumentsList.setContexts(startContext, getLexerContext());
        }
        else {
            typeArgumentsList = null;
        }

        return typeArgumentsList;
    }

    final void tryParseGenericTypeParameters() throws IOException, ParserException {
        
        if (lexer.lexSkipWS(JavaToken.LT) == JavaToken.LT) {

            final int startContext = writeCurContext();
            
            parseGenericTypeParameters(startContext);
        }
    }
    
    final void parseGenericTypeParameters(int startContext) throws IOException, ParserException {
        
        listener.onGenericTypeParametersStart(startContext);

        parseGenericTypeParameters(startContext, this::parseTypeReference);
        
        listener.onGenericTypeParametersEnd(startContext, getLexerContext());
    }
    
    @FunctionalInterface
    interface ParseFunction {
        
        void parse() throws IOException, ParserException;
    }

    private void parseGenericTypeParameters(int startContext, ParseFunction parseTypeReference) throws IOException, ParserException {
        
        for (;;) {

            parseTypeReference.parse();
            
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
    }
}
