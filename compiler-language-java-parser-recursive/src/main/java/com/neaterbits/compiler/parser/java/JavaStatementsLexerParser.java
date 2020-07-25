package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.cached.expressions.ExpressionCache;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgumentsList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

public abstract class JavaStatementsLexerParser<COMPILATION_UNIT>
    extends JavaParametersLexerParser<COMPILATION_UNIT> {

    private final TypeScratchInfo typeScratchInfo;

    JavaStatementsLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
        
        this.typeScratchInfo = new TypeScratchInfo();
    }
    
    private static final JavaToken [] STATEMENT_TOKENS = new JavaToken [] {

            // Local variables
            JavaToken.BYTE,
            JavaToken.SHORT,
            JavaToken.INT,
            JavaToken.LONG,
            JavaToken.FLOAT,
            JavaToken.DOUBLE,
            JavaToken.CHAR,
            JavaToken.BOOLEAN,

            // Statements
            JavaToken.IF,

            JavaToken.WHILE,
            JavaToken.FOR,
            
            JavaToken.RETURN,
            JavaToken.THROW,

            // Empty statement
            JavaToken.SEMI
    };

    private boolean parseStatement() throws ParserException, IOException {
        
        boolean foundStatement = true;
        
        final JavaToken statementToken = lexer.lexSkipWSAndComment(STATEMENT_TOKENS);
        
        switch (statementToken) {
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case CHAR:
        case BOOLEAN: {
            final int typeNameContext = writeCurContext();
            final long typeName = getStringRef();
            
            parseNonScopedTypeVariableDeclarationStatement(typeNameContext, typeName, ReferenceType.SCALAR);
            break;
        }
        
        case IF: {
            final int ifKeywordContext = writeCurContext();
            
            parseIfElseIfElse(getStringRef(), ifKeywordContext);
            break;
        }
        
        case WHILE: {
            final int whileKeywordContext = writeCurContext(); 
        
            parseWhile(getStringRef(), whileKeywordContext);
            break;
        }

        case FOR: {
            final int forKeywordContext = writeCurContext(); 
        
            parseFor(getStringRef(), forKeywordContext);
            break;
        }

        case RETURN: {
            
            final int returnKeywordContext = writeCurContext();
            
            parseReturn(getStringRef(), returnKeywordContext);
            break;
        }
        
        case THROW: {
            
            final int throwKeywordContext = writeCurContext();
            
            parseThrow(getStringRef(), throwKeywordContext);
            break;
        }

        case SEMI:
            // Only ';'
            break;
            
        default:
            foundStatement = parseExpressionOrVariableDeclaration();
            break;
        }

        return foundStatement;
    }
    
    private static Names getVariableDeclarationTypeAndClearExpressionCache(ExpressionCache expressionCache) {
        
        final Names result;
        
        if (expressionCache.areAllTopLevelNames()) {
            result = expressionCache.getTopLevelNames();

            expressionCache.clear();
        }
        else {
            result = null;
        }
        
        return result;
    }

    private boolean parseExpressionOrVariableDeclaration() throws IOException, ParserException {
        
        final boolean foundStatement;
        
        final int startContext = writeCurContext();
        
        final boolean expressionFound = parseExpressionStatementToCache();
        
        Names variableDeclarationNames;
        
        if (!expressionFound) {
            foundStatement = false;
        }
        else if ((variableDeclarationNames = getVariableDeclarationTypeAndClearExpressionCache(baseClassExpressionCache)) != null) {
            // Only names like com.test.SomeClass.staticVariable

            parseVariableDeclarationStatement(startContext, variableDeclarationNames);
            
            foundStatement = true;
        }
        else {
            // This is an expression statement
            listener.onExpressionStatementStart(startContext);
        
            applyAndClearExpressionCache(baseClassExpressionCache);
            
            listener.onExpressionStatementEnd(startContext, getLexerContext());

            foundStatement = true;
        }
        
        return foundStatement;
    }
    
    private void parseVariableDeclarationStatement(int statementContext, Names names) throws IOException, ParserException {
        
        listener.onVariableDeclarationStatementStart(statementContext);

        final int typeReferenceStartContext = listenerHelper.callScopedTypeReferenceListenersStartAndPart(
                names,
                ReferenceType.REFERENCE,
                null);
        
        // Generic type?
        tryParseGenericTypeArguments();

        listenerHelper.callScopedTypeReferenceListenersEnd(typeReferenceStartContext, getLexerContext());

        parseVariableDeclaratorList();
        
        listener.onVariableDeclarationStatementEnd(statementContext, getLexerContext());
    }

    //private static JavaToken [] VARIABLE_DECLARATION_STATEMENT_TOKENS = new JavaToken [] {
    //        JavaToken.COMMA
    // };
    
    private void parseNonScopedTypeVariableDeclarationStatement(
            int typeContext,
            long typeName,
            ReferenceType referenceType) throws ParserException, IOException {

        final int statementStartContext = writeContext(typeContext);
        
        listener.onVariableDeclarationStatementStart(statementStartContext);
        
        if (referenceType.isLeaf()) {
            listener.onLeafTypeReference(typeContext, typeName, referenceType);
        }
        else {
            listener.onNonScopedTypeReferenceStart(typeContext, typeName, referenceType);
            
            listener.onNonScopedTypeReferenceEnd(typeContext, getLexerContext());
        }

        parseVariableDeclaratorList();
        
        listener.onVariableDeclarationStatementEnd(statementStartContext, getLexerContext());
    }

    private void parseIfElseIfElse(long ifKeyword, int ifKeywordContext) throws IOException, ParserException {

        final int ifStartContext = writeContext(ifKeywordContext);
        final int ifStartConditionBlockContext = writeContext(ifKeywordContext);
        
        listener.onIfStatementStart(ifStartContext, ifKeyword, ifKeywordContext);
        
        listener.onIfStatementInitialBlockStart(ifStartConditionBlockContext);
        
        parseConditionInParenthesis();

        parseStatementOrBlock();

        listener.onIfStatementInitialBlockEnd(ifStartContext, getLexerContext());
        
        for (;;) {
            final JavaToken elseToken = lexer.lexSkipWS(JavaToken.ELSE);
            
            if (elseToken == JavaToken.ELSE) {
    
                final long elseKeyword = getStringRef();
    
                final int elseStatementStartContext = writeCurContext();
                final int elseKeywordContext = writeCurContext();
                
                // Is this an else-if?
                final JavaToken ifToken = lexer.lexSkipWS(JavaToken.IF);
                
                if (ifToken == JavaToken.IF) {
                    final int elseIfStatementStartContext = elseStatementStartContext;

                    final int elseIfKeywordContext = writeCurContext();
                    final long elseIfKeyword = getStringRef();
    
                    listener.onElseIfStatementStart(
                            elseIfStatementStartContext,
                            elseKeyword, elseKeywordContext,
                            elseIfKeyword, elseIfKeywordContext);
                    
                    parseConditionInParenthesis();
                 
                    parseStatementOrBlock();
                    
                    listener.onElseIfStatementEnd(elseIfStatementStartContext, getLexerContext());
                }
                else {
                    // This was an 'else', not an 'else if'
                    listener.onElseStatementStart(elseStatementStartContext, elseKeyword, elseKeywordContext);
                    
                    parseStatementOrBlock();
                    
                    listener.onElseStatementEnd(elseStatementStartContext, getLexerContext());
                    
                    listener.onEndIfStatement(ifStartContext, getLexerContext());
                    
                    break; // last statement in 'if - else if - else' so break out
                }
            }
            else {
                // No 'else' keyword
                listener.onEndIfStatement(ifStartContext, getLexerContext());
                
                break; // 'last statement in 'if - else' so break out
            }
        }
    }

    private void parseWhile(long whileKeyword, int whileKeywordContext) throws IOException, ParserException {
        
        final int whileStartContext = writeContext(whileKeywordContext);
        
        listener.onWhileStatementStart(whileStartContext, whileKeyword, whileKeywordContext);
        
        parseConditionInParenthesis();
        
        parseStatementOrBlock();
        
        listener.onWhileStatementEnd(whileStartContext, getLexerContext());
    }

    private void parseFor(long forKeyword, int forKeywordContext) throws IOException, ParserException {
        
        final int forStartContext = writeContext(forKeywordContext);
        
        if (lexer.lexSkipWS(JavaToken.LPAREN) != JavaToken.LPAREN) {
            throw lexer.unexpectedToken();
        }

        final boolean expressionFound = parseExpressionStatementToCache();
        
        Names variableDeclarationNames;
        
        if (!expressionFound) {
            throw new ParserException("No expression found in for-statement");
        }
        else if ((variableDeclarationNames = getVariableDeclarationTypeAndClearExpressionCache(baseClassExpressionCache)) != null) {

            final Context namesEndContext = initScratchContext();
            
            try {
                parseForWithNames(forStartContext, forKeyword, forKeywordContext, variableDeclarationNames, namesEndContext);
            }
            finally {
                freeScratchContext(namesEndContext);
            }
        }
        else {
            throw lexer.unexpectedToken();
        }
    }
    
    private void parseForWithNames(
            int forStartContext,
            long forKeyword, int forKeywordContext,
            Names variableDeclarationNames,
            Context namesEndContext) throws IOException, ParserException {

        // Only names like com.test.SomeClass.staticVariable
        if (lexer.lexSkipWS(JavaToken.IDENTIFIER) == JavaToken.IDENTIFIER) {

            final long identifier = getStringRef();
            final int identifierContext = writeCurContext();

            final TypeArgumentsList typeArguments = tryParseGenericTypeArgumentsToScratchList();
            
            final Context typeEndContext = initScratchContext();
            
            try {
                final Context variableDeclaratorEndContext = initScratchContext();
                
                try {
                    
                    if (lexer.lexSkipWS(JavaToken.COLON) == JavaToken.COLON) {
                        
                        listener.onIteratorForStatementStart(forStartContext, forKeyword, forKeywordContext);
    
                        typeScratchInfo.initScopedOrNonScoped(variableDeclarationNames, namesEndContext, ReferenceType.REFERENCE);
                        
                        listenerHelper.onTypeAndOptionalArgumentsList(typeScratchInfo, typeArguments, typeEndContext);
                        
                        listener.onVariableName(identifierContext, identifier, 0);
                        
                        final int iteratorForTestStartContext = writeCurContext();
                        
                        listener.onIteratorForTestStart(iteratorForTestStartContext);
                        
                        parseExpression();
                        
                        listener.onIteratorForTestEnd(iteratorForTestStartContext, getLexerContext());
                        
                        if (lexer.lexSkipWS(JavaToken.RPAREN) != JavaToken.RPAREN) {
                            throw lexer.unexpectedToken();
                        }
                        
                        parseStatementOrBlock();
    
                        listener.onIteratorForStatementEnd(forStartContext, getLexerContext());
                    }
                    else {
                        throw lexer.unexpectedToken();
                    }
                }
                finally {
                    freeScratchContext(variableDeclaratorEndContext);
                }
            }
            finally {
                freeScratchContext(typeEndContext);
            }
            
        }
        else {
            throw lexer.unexpectedToken();
        }
    }

    private void parseReturn(long returnKeyword, int returnKeywordContext) throws IOException, ParserException {
        
        final int returnStartContext = writeContext(returnKeywordContext);
        
        listener.onReturnStatementStart(returnStartContext, returnKeyword, returnKeywordContext);

        parseExpression();
        
        listener.onReturnStatementEnd(returnStartContext, getLexerContext());
    }

    private void parseThrow(long throwKeyword, int throwKeywordContext) throws IOException, ParserException {
        
        final int throwStartContext = writeContext(throwKeywordContext);
        
        listener.onThrowStatementStart(throwStartContext, throwKeyword, throwKeywordContext);

        parseExpression();
        
        listener.onThrowStatementEnd(throwStartContext, getLexerContext());
    }

    private void parseStatementOrBlock() throws ParserException, IOException {

        final JavaToken optionalLBrace = lexer.lexSkipWS(JavaToken.LBRACE);
        
        if (optionalLBrace == JavaToken.LBRACE) {
            parseBlockAndRBrace();
        }
        else {
            parseStatement();
        }
    }

    final void parseBlockAndRBrace() throws ParserException, IOException {

        parseBlock();
        
        final JavaToken rbraceToken = lexer.lexSkipWS(JavaToken.RBRACE);
        
        if (rbraceToken != JavaToken.RBRACE) {
            throw lexer.unexpectedToken();
        }
    }
    
    private void parseBlock() throws ParserException, IOException {
        
        boolean done = false;
        
        do {
            final boolean statementFound = parseStatement();
            
            done = !statementFound;
            
        } while (!done);
    }
}
