package dev.nimbler.compiler.parser.java.recursive;

import java.io.IOException;

import org.jutils.ArrayUtils;
import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.Tokenizer;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.parser.recursive.cached.expressions.ExpressionCache;
import dev.nimbler.compiler.parser.recursive.cached.names.NamesList;
import dev.nimbler.compiler.parser.recursive.cached.types.TypeArgumentsList;
import dev.nimbler.compiler.types.ReferenceType;
import dev.nimbler.compiler.types.statement.ASTMutability;
import dev.nimbler.compiler.util.Base;
import dev.nimbler.compiler.util.ContextRef;
import dev.nimbler.compiler.util.name.Names;

public abstract class JavaStatementsLexerParser<COMPILATION_UNIT>
    extends JavaParametersLexerParser<COMPILATION_UNIT> {

    private final TypeScratchInfo typeScratchInfo;

    JavaStatementsLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
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
            JavaToken.SWITCH,
            JavaToken.BREAK,

            JavaToken.WHILE,
            JavaToken.FOR,
            
            JavaToken.RETURN,
            JavaToken.THROW,
            
            JavaToken.TRY,

            // Empty statement
            JavaToken.SEMI
    };

    private static final JavaToken [] AFTER_TRY_TOKENS = new JavaToken [] {
            JavaToken.LBRACE,
            JavaToken.LPAREN
    };

    private boolean parseStatement() throws ParserException, IOException {

        final JavaToken statementToken = lexer.lexSkipWSAndComment(STATEMENT_TOKENS);

        return parseStatement(statementToken);
    }

    private boolean parseStatement(JavaToken statementToken) throws ParserException, IOException {

        boolean foundStatement = true;

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
        
        case SWITCH: {

            final int switchKeywordContext = writeCurContext();
            
            parseSwitchCase(getStringRef(), switchKeywordContext);
            break;
        }
        
        case BREAK: {

            final int breakContext = writeCurContext();
            
            parseBreak(breakContext, getStringRef(), breakContext);
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

        case TRY: {
            final int tryKeywordContext = writeCurContext(); 
        
            final JavaToken javaToken = lexer.lexSkipWS(AFTER_TRY_TOKENS);
            
            switch (javaToken) {
            case LBRACE:
                parseTryCatchFinallyWithoutLBrace(getStringRef(), tryKeywordContext);
                break;
                
            case LPAREN:
                parseTryWithResourcesWithoutLParen(getStringRef(), tryKeywordContext);
                break;
                
            default:
                throw lexer.unexpectedToken();
            }
            
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

        final boolean finalModifier = lexer.lexSkipWS(JavaToken.FINAL) == JavaToken.FINAL;
        
        if (finalModifier) {
            
            final int finalContext = writeCurContext();
            
            final NamesList namesList = startScratchNameParts();
            
            try {
                parseNames(namesList);
            }
            finally {
                namesList.complete(names -> parseVariableDeclarationStatement(startContext, finalModifier, finalContext, names));
            }
         
            foundStatement = true;
        }
        else {
            
            final boolean expressionFound = parseExpressionStatementToCache();
            
            Names variableDeclarationNames;
            
            if (!expressionFound) {
                foundStatement = false;
            }
            else if ((variableDeclarationNames = getVariableDeclarationTypeAndClearExpressionCache(baseClassExpressionCache)) != null) {
                // Only names like com.test.SomeClass.staticVariable
    
                parseVariableDeclarationStatement(startContext, false, ContextRef.NONE, variableDeclarationNames);
                
                foundStatement = true;
            }
            else {
                // This is an expression statement
                listener.onExpressionStatementStart(startContext);
            
                applyAndClearExpressionCache(baseClassExpressionCache);
                
                listener.onExpressionStatementEnd(startContext, getLexerContext());
    
                foundStatement = true;
            }
        }
        
        return foundStatement;
    }
    
    private void parseVariableDeclarationStatement(int statementContext, boolean finalModifier, int finalContext, Names names) throws IOException, ParserException {
        
        listener.onVariableDeclarationStatementStart(statementContext);
        
        if (finalModifier) {
            
            listener.onMutabilityVariableModifier(finalContext, ASTMutability.VALUE_OR_REF_IMMUTABLE);
        }

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
    
    // 'case', 'default' or '}' at end of switch-case 
    private static final JavaToken [] SWITCH_CASE_TOKENS = {
            JavaToken.CASE,
            JavaToken.DEFAULT,
            JavaToken.RBRACE
    };
    
    private static final JavaToken [] STATEMENT_AND_SWITCH_CASE_TOKENS
    = ArrayUtils.merge(STATEMENT_TOKENS, SWITCH_CASE_TOKENS);

    private void parseSwitchCase(long switchKeyword, int switchKeywordContext) throws IOException, ParserException {

        final int switchStartContext = writeContext(switchKeywordContext);
        
        listener.onSwitchStatementStart(switchStartContext, switchKeyword, switchKeywordContext);

        if (lexer.lexSkipWS(JavaToken.LPAREN) != JavaToken.LPAREN) {
            throw lexer.parserError("Expected '(' before switch");
        }
        
        if (!parseExpression()) {
            throw lexer.parserError("Expected switch expression");
        }
        
        if (lexer.lexSkipWS(JavaToken.RPAREN) != JavaToken.RPAREN) {
            throw lexer.parserError("Expected ')' after switch");
        }

        if (lexer.lexSkipWS(JavaToken.LBRACE) != JavaToken.LBRACE) {
            throw lexer.parserError("Expected '{' after switch ()");
        }
        
        final int switchBlockStartContext = writeCurContext();

        listener.onJavaSwitchBlockStart(switchBlockStartContext);

        parseSwitchBlock();
            
        listener.onJavaSwitchBlockEnd(switchBlockStartContext, getLexerContext());

        listener.onSwitchStatementEnd(switchStartContext, getLexerContext());
    }
    
    private void parseSwitchBlock() throws IOException, ParserException {
        
        boolean lbrace = false;
        
        int groupLabelCount = 0;
        int statementCount = 0;

        int labelsStartContext = ContextRef.NONE;
        
        do {
            final JavaToken caseDefaultStatement = lexer.lexSkipWS(STATEMENT_AND_SWITCH_CASE_TOKENS);

            switch (caseDefaultStatement) {
            case CASE:
            case DEFAULT:

                final int caseOrDefaultContext = writeCurContext();

                final int caseOrDefaultKeywordContext = writeCurContext();
                
                // End of last block
                if (statementCount > 0) {
                    listener.onJavaSwitchBlockStatementGroupEnd(groupLabelCount, getLexerContext());

                    // Switched to new group
                    statementCount = 0;
                }

                if (groupLabelCount == 0) {
                    
                    labelsStartContext = caseOrDefaultContext;
                    
                    listener.onJavaSwitchBlockStatementGroupStart(labelsStartContext);

                    listener.onSwitchLabelsStart(labelsStartContext);
                }
                
                ++ groupLabelCount;
                
                if (caseDefaultStatement == JavaToken.CASE) {
                    parseCaseLabel(caseOrDefaultContext, getStringRef(), caseOrDefaultKeywordContext);
                }
                else {
                    listener.onDefaultSwitchLabel(caseOrDefaultContext, getStringRef());
                }

                if (lexer.lexSkipWS(JavaToken.COLON) != JavaToken.COLON) {
                    throw lexer.unexpectedToken();
                }
                break;

            case RBRACE:
                listener.onJavaSwitchBlockStatementGroupEnd(groupLabelCount, getLexerContext());
                listener.onSwitchLabelsEnd(labelsStartContext, getLexerContext());

                lbrace = true;
                break;

            default:
                if (groupLabelCount > 0) {
                    listener.onSwitchLabelsEnd(labelsStartContext, getLexerContext());

                    groupLabelCount = 0;
                }
                else {
                    // Initial statement with no labels is a parse error 
                    if (statementCount == 0) {
                        throw lexer.parserError("Statement without label within switch");
                    }
                }
                
                parseStatement(caseDefaultStatement);
                
                ++ statementCount;
                break;
            }
        }
        while (!lbrace);
    }

    private static final JavaToken [] CASE_LABEL_TOKENS = new JavaToken [] {
            JavaToken.DECIMAL_LITERAL,
            JavaToken.STRING_LITERAL,
            JavaToken.CHARACTER_LITERAL,
            JavaToken.IDENTIFIER
    };
    
    private void parseCaseLabel(int caseStartContext, long caseKeyword, int caseKeywordContext) throws IOException, ParserException {
     
        final JavaToken caseExpressionToken = lexer.lexSkipWS(CASE_LABEL_TOKENS);
        
        final int labelStartContext = writeCurContext();

        switch (caseExpressionToken) {
        case DECIMAL_LITERAL:
            listener.onConstantSwitchLabelStart(caseStartContext, caseKeyword, caseKeywordContext);
            
            listener.onIntegerLiteral(
                    labelStartContext,
                    tokenizer.asInteger(getStringRef()),
                    Base.DECIMAL,
                    true,
                    32);
            
            listener.onConstantSwitchLabelEnd(caseStartContext, getLexerContext());
            break;
            
        case STRING_LITERAL:
            listener.onConstantSwitchLabelStart(caseStartContext, caseKeyword, caseKeywordContext);
            
            listener.onStringLiteral(labelStartContext, getStringRef());
            
            listener.onConstantSwitchLabelEnd(caseStartContext, getLexerContext());
            break;
            
        case CHARACTER_LITERAL:
            listener.onConstantSwitchLabelStart(caseStartContext, caseKeyword, caseKeywordContext);

            listener.onCharacterLiteral(labelStartContext, tokenizer.asCharacter(getStringRef(), 1));

            listener.onConstantSwitchLabelEnd(caseStartContext, getLexerContext());
            break;

        case IDENTIFIER:
            listener.onEnumSwitchLabel(
                    caseStartContext,
                    caseKeyword, caseKeywordContext,
                    getStringRef(), writeCurContext());
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }

    private void parseBreak(int startContext, long breakKeyword, int breakKeywordContext) throws IOException, ParserException {
        
        // Identifier for label?
        
        final long breakLabel;
        final int breakLabelContext;
        
        if (lexer.lexSkipWS(JavaToken.IDENTIFIER) == JavaToken.IDENTIFIER) {
            breakLabel = getStringRef();
            breakLabelContext = writeCurContext();
        }
        else {
            breakLabel = StringRef.STRING_NONE;
            breakLabelContext = ContextRef.NONE;
        }
        
        listener.onBreakStatement(
                startContext,
                breakKeyword, breakKeywordContext,
                breakLabel, breakLabelContext,
                getLexerContext());
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
        
        final int forCriteriaStartContext = writeCurContext();

        final boolean expressionFound = parseExpressionStatementToCache();
        
        Names variableDeclarationNames;
        
        if (!expressionFound) {
            // for (; i < 10; ++ i)
            parseBasicForWithNoForInit(forStartContext, forKeyword, forKeywordContext);
        }
        else if ((variableDeclarationNames = getVariableDeclarationTypeAndClearExpressionCache(baseClassExpressionCache)) != null) {

            final Context namesEndContext = initScratchContext();
            
            try {
                parseForWithNames(
                        forStartContext,
                        forKeyword, forKeywordContext,
                        forCriteriaStartContext,
                        variableDeclarationNames, namesEndContext);
            }
            finally {
                freeScratchContext(namesEndContext);
            }
        }
        else {
            // with expression in forInit
            listener.onForStatementStart(forStartContext, forKeyword, forKeywordContext);

            listener.onForInitStart(forCriteriaStartContext);
            
            final int expressionListStartContext = writeContext(forCriteriaStartContext);
            
            listener.onExpressionListStart(expressionListStartContext);
            
            applyAndClearExpressionCache(baseClassExpressionCache);
            
            if (lexer.lexSkipWS(JavaToken.SEMI) != JavaToken.SEMI) {
                throw lexer.unexpectedToken();
            }
            
            listener.onExpressionListEnd(expressionListStartContext, getLexerContext());
            
            listener.onForInitEnd(forCriteriaStartContext, getLexerContext());
            
            parseBasicForExpressionAndUpdate();
            
            parseStatementOrBlock();
            
            listener.onForStatementEnd(forStartContext, getLexerContext());
        }
    }
    
    private void parseForWithNames(
            int forStartContext,
            long forKeyword, int forKeywordContext,
            int forCriteriaStartContext,
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

                        parseIteratorFor(
                                forStartContext,
                                forKeyword, forKeywordContext,
                                variableDeclarationNames, namesEndContext,
                                typeArguments, typeEndContext,
                                identifier, identifierContext);
                        
                    }
                    else {

                        parseBasicForWithLocalVariableDeclaration(
                                forStartContext,
                                forKeyword, forKeywordContext,
                                forCriteriaStartContext,
                                variableDeclarationNames, namesEndContext,
                                typeArguments, typeEndContext,
                                identifier, identifierContext);
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

    private void parseBasicForWithLocalVariableDeclaration(
            int forStartContext,
            long forKeyword, int forKeywordContext,
            int forCriteriaStartContext,
            Names variableDeclarationNames, Context namesEndContext,
            TypeArgumentsList typeArguments, Context typeEndContext,
            long identifier, int identifierContext) throws IOException, ParserException {
        
        listener.onForStatementStart(forStartContext, forKeyword, forKeywordContext);
        
        final int forInitStartContext = writeContext(forCriteriaStartContext);
        
        listener.onForInitStart(forInitStartContext);

        typeScratchInfo.initScopedOrNonScoped(variableDeclarationNames, namesEndContext, ReferenceType.REFERENCE);
        
        listenerHelper.onTypeAndOptionalArgumentsList(typeScratchInfo, typeArguments, typeEndContext);

        if (lexer.lexSkipWS(JavaToken.ASSIGN) != JavaToken.ASSIGN) {
            throw lexer.unexpectedToken();
        }

        final boolean done = parseVariableInitializer(identifier, identifierContext);
        if (!done) {
            parseVariableDeclaratorList();
        }
        
        listener.onForInitEnd(forInitStartContext, getLexerContext());

        parseBasicForExpressionAndUpdate();

        parseStatementOrBlock();

        listener.onForStatementEnd(forStartContext, getLexerContext());
    }
    
    private void parseBasicForWithNoForInit(
            int forStartContext,
            long forKeyword, int forKeywordContext) throws IOException, ParserException {

        listener.onForStatementStart(forStartContext, forKeyword, forKeywordContext);
        
        if (lexer.lexSkipWS(JavaToken.SEMI) != JavaToken.SEMI) {
            throw lexer.unexpectedToken();
        }

        parseBasicForExpressionAndUpdate();

        parseStatementOrBlock();

        listener.onForStatementEnd(forStartContext, getLexerContext());
    }
    
    private void parseBasicForExpressionAndUpdate() throws IOException, ParserException {
        
        final int forExpressionStartContext = writeCurContext();

        listener.onForExpressionStart(forExpressionStartContext);

        parseExpression();

        listener.onForExpressionEnd(forExpressionStartContext, getLexerContext());

        if (lexer.lexSkipWS(JavaToken.SEMI) != JavaToken.SEMI) {
            throw lexer.unexpectedToken();
        }
        
        final int forUpdateStartContext = writeCurContext();
        
        listener.onForUpdateStart(forUpdateStartContext);
        
        parseStatementExpressionList();
        
        listener.onForUpdateEnd(forUpdateStartContext, getLexerContext());
        
        if (lexer.lexSkipWS(JavaToken.RPAREN) != JavaToken.RPAREN) {
            throw lexer.unexpectedToken();
        }
    }
    
    private void parseStatementExpressionList() throws IOException, ParserException {
        
        for (;;) {
            
            parseStatementExpression();
            
            if (lexer.lexSkipWS(JavaToken.COMMA) != JavaToken.COMMA) {
                break;
            }
        }
    }
    
    private void parseStatementExpression() throws IOException, ParserException {
        
        parseExpression();
    }

    private void parseIteratorFor(
            int forStartContext,
            long forKeyword,
            int forKeywordContext,
            Names variableDeclarationNames,
            Context namesEndContext,
            TypeArgumentsList typeArguments,
            Context typeEndContext,
            long identifier, int identifierContext) throws IOException, ParserException {
        
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
    
    private static final JavaToken [] CATCH_OR_FINALLY_TOKENS = new JavaToken [] {

            JavaToken.CATCH,
            JavaToken.FINALLY
    };

    private void parseTryCatchFinallyWithoutLBrace(long tryKeyword, int tryKeywordContext) throws IOException, ParserException {
        
        final int tryCatchFinallyStartContext = writeContext(tryKeywordContext);
        
        final int tryBlockStartContext = writeContext(tryKeywordContext);
        
        listener.onTryStatementStart(tryCatchFinallyStartContext, tryKeyword, tryKeywordContext);

        parseBlockStatementsAndRBrace();
        
        listener.onTryBlockEnd(tryBlockStartContext, getLexerContext());

        parseCatchesAndFinally();
        
        listener.onTryStatementEnd(tryCatchFinallyStartContext, getLexerContext());
    }
    
    private void parseTryWithResourcesWithoutLParen(long tryKeyword, int tryKeywordContext) throws IOException, ParserException {
        
        final int tryWithResourcesStartContext = writeContext(tryKeywordContext);
        
        final int tryBlockStartContext = writeContext(tryKeywordContext);
        
        listener.onTryWithResourcesStatementStart(tryWithResourcesStartContext, tryKeyword, tryKeywordContext);
        
        parseResourcesSpecification();

        parseBlock();
        
        listener.onTryBlockEnd(tryBlockStartContext, getLexerContext());
        
        parseCatchesAndFinally();
        
        listener.onTryWithResourcesEnd(tryWithResourcesStartContext, getLexerContext());
    }
    
    private static final JavaToken [] AFTER_RESOURCE_TOKENS = new JavaToken [] {
            
            JavaToken.SEMI,
            
            JavaToken.RPAREN
    };
    
    private void parseResourcesSpecification() throws IOException, ParserException {
        
        final int startContext = writeCurContext();
        
        listener.onTryWithResourcesSpecificationStart(startContext);
        
        boolean done = false;
        
        do {
            parseResource();
            
            final JavaToken javaToken = lexer.lexSkipWS(AFTER_RESOURCE_TOKENS);
            
            switch (javaToken) {
            case SEMI:
                break;
                
            case RPAREN:
                done = true;
                break;
                
            default:
                throw lexer.unexpectedToken();
            }

        } while (!done);
        
        listener.onTryWithResourcesSpecificationEnd(startContext, getLexerContext());
    }
    
    private void parseResource() throws IOException, ParserException {
        
        final int startContext = writeCurContext();
        
        listener.onResourceStart(startContext);
        
        parseTypeReference();
        
        parseVariableName();
        
        if (lexer.lexSkipWS(JavaToken.ASSIGN) != JavaToken.ASSIGN) {
            throw lexer.unexpectedToken();
        }
        
        parseExpression();
        
        listener.onResourceEnd(startContext, getLexerContext());
    }
    
    private void parseCatchesAndFinally() throws IOException, ParserException {

        boolean done = false;
        
        do {
            final JavaToken javaToken = lexer.lexSkipWS(CATCH_OR_FINALLY_TOKENS);
            
            switch (javaToken) {
            case CATCH: {
             
                final int catchStartContext = writeCurContext(); 
                
                listener.onCatchStart(catchStartContext, getStringRef(), writeCurContext());

                parseCatchExceptions();
                
                parseBlock();
                
                listener.onCatchEnd(catchStartContext, getLexerContext());
                break;
            }
                
            case FINALLY: {
             
                final int startContext = writeCurContext();
                
                listener.onFinallyStart(startContext, getStringRef(), writeCurContext());
                
                parseBlock();
                
                listener.onFinallyEnd(startContext, getLexerContext());
                break;
            }
                
            case NONE:
                done = true;
                break;
                
            default:
                throw lexer.unexpectedToken();
            }
        }
        while (!done);

    }
    
    private void parseCatchExceptions() throws IOException, ParserException {
        
        if (lexer.lexSkipWS(JavaToken.LPAREN) != JavaToken.LPAREN) {
            throw lexer.unexpectedToken();
        }
     
        for (;;) {

            parseTypeReference();
            
            if (lexer.lexSkipWS(JavaToken.BITWISE_OR) != JavaToken.BITWISE_OR) {
                break;
            }
        }
        
        parseVariableName();
        
        if (lexer.lexSkipWS(JavaToken.RPAREN) != JavaToken.RPAREN) {
            throw lexer.unexpectedToken();
        }
    }
    
    private void parseVariableName() throws IOException, ParserException {
        
        if (lexer.lexSkipWS(JavaToken.IDENTIFIER) != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }

        listener.onVariableName(writeCurContext(), getStringRef(), 0);
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
    
    private void parseBlock() throws ParserException, IOException {
        
        final JavaToken optionalLBrace = lexer.lexSkipWS(JavaToken.LBRACE);
        
        if (optionalLBrace == JavaToken.LBRACE) {
            parseBlockStatementsAndRBrace();
        }
        else {
            throw lexer.unexpectedToken();
        }
    }

    private void parseStatementOrBlock() throws ParserException, IOException {

        final JavaToken optionalLBrace = lexer.lexSkipWS(JavaToken.LBRACE);
        
        if (optionalLBrace == JavaToken.LBRACE) {
            parseBlockStatementsAndRBrace();
        }
        else {
            parseStatement();
        }
    }

    final void parseBlockStatementsAndRBrace() throws ParserException, IOException {

        parseBlockStatements();
        
        final JavaToken rbraceToken = lexer.lexSkipWS(JavaToken.RBRACE);
        
        if (rbraceToken != JavaToken.RBRACE) {
            throw lexer.unexpectedToken();
        }
    }

    private void parseBlockStatements() throws ParserException, IOException {
        
        boolean done = false;
        
        do {
            final boolean statementFound = parseStatement();
            
            done = !statementFound;
            
        } while (!done);
    }
}
