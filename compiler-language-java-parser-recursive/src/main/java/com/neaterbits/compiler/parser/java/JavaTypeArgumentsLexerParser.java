package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgumentsList;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaTypeArgumentsLexerParser<COMPILATION_UNIT> extends JavaTypesLexerParser<COMPILATION_UNIT> {

    JavaTypeArgumentsLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    static final JavaToken [] AFTER_TYPE_ARGUMENT_TOKENS = new JavaToken [] {
            JavaToken.COMMA,
            JavaToken.GT
    };
    
    final TypeArgumentsList tryParseGenericTypeArgumentsToScratchList() throws IOException, ParserException {
        
        final TypeArgumentsList typeArgumentsList;
        
        if (lexer.lexSkipWS(JavaToken.LT) == JavaToken.LT) {

            final int typeArgumentsStartContext = writeCurContext();

            typeArgumentsList = startScratchTypeArguments();

            for (;;) {
            
                parseGenericTypeArgumentToScratchList(typeArgumentsList);

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

            typeArgumentsList.setContexts(typeArgumentsStartContext, getLexerContext());
        }
        else {
            typeArgumentsList = null;
        }

        return typeArgumentsList;
    }
    
    private void parseGenericTypeArgumentToScratchList(TypeArgumentsList typeArgumentsList) throws IOException, ParserException {

        if (lexer.lexSkipWS(JavaToken.QUESTION_MARK) == JavaToken.QUESTION_MARK) {
            typeArgumentsList.addWildcardType(writeCurContext(), getLexerContext());
        }
        else {
            // Parse the type to names list
            
            final int startContext = writeCurContext();
            
            final NamesList namesList = startScratchNameParts();
            
            parseNames(namesList);

            final TypeArgumentsList genericTypes = tryParseGenericTypeArgumentsToScratchList();
            
            typeArgumentsList.addReferenceType(startContext, namesList, genericTypes, getLexerContext());
        }
    }

    @Override
    final void tryParseGenericTypeArguments() throws IOException, ParserException {
        
        if (lexer.lexSkipWS(JavaToken.LT) == JavaToken.LT) {

            final int startContext = writeCurContext();
            
            parseGenericTypeArguments(startContext);
        }
    }
    
    private void parseGenericTypeArguments(int startContext) throws IOException, ParserException {
        
        listener.onGenericTypeArgumentsStart(startContext);

        parseGenericTypeArguments(startContext, this::parseTypeReference);
        
        listener.onGenericTypeArgumentsEnd(startContext, getLexerContext());
    }
    
    @FunctionalInterface
    interface ParseFunction {
        
        void parse() throws IOException, ParserException;
    }

    private void parseGenericTypeArguments(int startContext, ParseFunction parseTypeReference) throws IOException, ParserException {
        
        for (;;) {

            final int curContext = writeCurContext();
            
            listener.onGenericReferenceTypeArgumentStart(curContext);
            
            parseTypeReference.parse();
            
            listener.onGenericReferenceTypeArgumentEnd(curContext, getLexerContext());
            
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
