package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.ImmutableContext;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

public abstract class JavaStatementsLexerParser<COMPILATION_UNIT>
    extends JavaVariablesLexerParser<COMPILATION_UNIT> {

    JavaStatementsLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
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

            JavaToken.IF,
            JavaToken.WHILE,

            JavaToken.IDENTIFIER,
            
            JavaToken.SEMI
    };

    private static final JavaToken [] STATEMENT_STARTING_WITH_IDENTIFIER_TOKENS = new JavaToken [] {
            JavaToken.PERIOD, // User type for variable declaration or method invocation or field dereferencing
            JavaToken.ASSIGN, // Assignment
            JavaToken.IDENTIFIER, // eg. SomeType varName;
            JavaToken.LPAREN,
            JavaToken.LT // generic type
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
        case CHAR: {
            final int typeNameContext = writeCurContext();
            final long typeName = getStringRef();
            
            parseNonScopedTypeVariableDeclarationStatement(typeNameContext, typeName, ReferenceType.SCALAR);
            break;
        }
        
        case IDENTIFIER: {
            final int identifierContext = writeCurContext();
            
            final int statementContext = writeContext(identifierContext);

            final long identifier = getStringRef();

            final JavaToken statementStartingWithIdentifierToken = lexer.lexSkipWS(STATEMENT_STARTING_WITH_IDENTIFIER_TOKENS);

            // TODO try to remove
            final Context afterIdentifierContext = new ImmutableContext(getLexerContext());
            
            switch (statementStartingWithIdentifierToken) {
            case PERIOD:
                
                // Identifier, then '.' can be many things like
                //
                // SomeClass.staticMethod()
                // STATIC_MEMBER.staticMethod();
                // a.complete.packagagepath.SomeClass.staticMethod()
                // a.complete.Type varName
                // etc
                // So just parse all names and the look at what tokens shows up after that 
                
                parseNameListUntilOtherToken(
                        identifierContext,
                        identifier,
                        names -> parseNameListAfterStatementIdentifiers(
                                statementContext,
                                identifier,
                                identifierContext,
                                names));
                
                /*
                listener.onVariableDeclarationStatementStart(statementContext);
    
                parseUserTypeAfterPeriod(identifierContext, identifier);
                
                parseVariableDeclaratorList(writeCurContext());
                
                listener.onVariableDeclarationStatementEnd(statementContext, getLexerContext());
                */
                break;
                
            case ASSIGN: {
                // variable = value;
                listener.onAssignmentStatementStart(statementContext);
                
                final int expressionContext = writeContext(statementContext);
                
                listener.onEnterAssignmentExpression(expressionContext);

                final int lhsStartContext = writeContext(statementContext);
                listener.onEnterAssignmentLHS(lhsStartContext);
                
                final int leafContext = writeContext(lhsStartContext);
                listener.onVariableReference(leafContext, identifier);
                
                listener.onExitAssignmentLHS(lhsStartContext, afterIdentifierContext);
                
                final Context endContext = getLexerContext();
                
                parseExpressionList();
                
                listener.onExitAssignmentExpression(expressionContext, endContext);
                
                listener.onAssignmentStatementEnd(statementContext, endContext);
                break;
            }
            
            case IDENTIFIER: {
                final long varName = getStringRef();
                final int varNameContext = writeCurContext();

                listener.onVariableDeclarationStatementStart(statementContext);
                
                listener.onNonScopedTypeReferenceStart(identifierContext, identifier, ReferenceType.REFERENCE);
                
                listener.onNonScopedTypeReferenceEnd(identifierContext, getLexerContext());

                parseVariableDeclaratorList(varName, varNameContext);
                
                listener.onVariableDeclarationStatementEnd(statementContext, getLexerContext());
                break;
            }
            
            case LPAREN: {
                // Method invocation
                listener.onExpressionStatementStart(statementContext);
                
                onMethodInvocationPrimaryList(
                        statementContext,
                        identifier,
                        identifierContext,
                        MethodInvocationType.NO_OBJECT,
                        null);
                
                listener.onExpressionStatementEnd(statementContext, getLexerContext());
                break;
            }
            
            case LT:
                listener.onVariableDeclarationStatementStart(statementContext);
                
                listener.onNonScopedTypeReferenceStart(identifierContext, identifier, ReferenceType.REFERENCE);

                parseGenericTypeParameters(writeCurContext());

                listener.onNonScopedTypeReferenceEnd(identifierContext, getLexerContext());

                parseVariableDeclaratorList();
                
                listener.onVariableDeclarationStatementEnd(statementContext, getLexerContext());
                break;
                
            default:
                throw lexer.unexpectedToken();
            }
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

        case SEMI:
            // Only ';'
            break;
            
        default:
            foundStatement = false;
            break;
        }

        return foundStatement;
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

    private void parseStatementOrBlock() throws ParserException, IOException {

        final JavaToken optionalLBrace = lexer.lexSkipWS(JavaToken.LBRACE);
        
        if (optionalLBrace == JavaToken.LBRACE) {
            parseBlockAndRBrace();
        }
        else {
            parseStatement();
        }
    }

    private static final JavaToken [] AFTER_NAMELIST_TOKENS = new JavaToken[] {

            JavaToken.IDENTIFIER, // eg. com.test.SomeType varName;
            JavaToken.LPAREN
            
    };
    
    private void parseNameListAfterStatementIdentifiers(
            int context,
            long identifier,
            int identifierContext,
            Names names) throws IOException, ParserException {
        
        if (names.count() <= 1) {
            throw new IllegalArgumentException();
        }

        // What is next token?
        final JavaToken afterNameListToken = lexer.lexSkipWS(AFTER_NAMELIST_TOKENS);

        switch (afterNameListToken) {
        case LPAREN:
            listener.onExpressionStatementStart(context);

            onMethodInvocationPrimaryList(
                    context,
                    identifier,
                    identifierContext,
                    MethodInvocationType.NAMED_CLASS_STATIC_OR_STATIC_VAR,
                    names);
            
            listener.onExpressionStatementEnd(context, getLexerContext());
            break;
            
        case IDENTIFIER:
            // some.scope.Type variableDeclaration
            listener.onVariableDeclarationStatementStart(context);
            
            listenerHelper.onType(ContextRef.NONE, StringRef.STRING_NONE, names, null, ReferenceType.REFERENCE, getLexerContext());

            parseVariableDeclaratorList(getStringRef(), writeCurContext());
            
            listener.onVariableDeclarationStatementEnd(context, getLexerContext());
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }

    private void onMethodInvocationPrimaryList(
            int context,
            long identifier,
            int identifierContext,
            MethodInvocationType methodInvocationType,
            Names names) throws IOException, ParserException {
        
        final int primaryListContext = writeContext(context);
        
        listener.onPrimaryStart(primaryListContext);
        
        final int methodInvocationContext = writeContext(context);
        
        listener.onMethodInvocationStart(
                methodInvocationContext,
                methodInvocationType,
                names,
                names != null ? names.count() - 1 : 0,
                names != null ? names.getStringAt(names.count() - 1) : identifier,
                names != null ? names.getContextAt(names.count() - 1) : identifierContext);
        
        parseMethodInvocationParameters();
        
        listener.onMethodInvocationEnd(methodInvocationContext, names == null, getLexerContext());
        
        parseAnyAdditionalPrimaries();
        
        listener.onPrimaryEnd(primaryListContext, getLexerContext());
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
