package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.parser.recursive.cached.expressions.ExpressionCache;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.operator.Arithmetic;
import com.neaterbits.compiler.types.operator.Assignment;
import com.neaterbits.compiler.types.operator.IncrementDecrement;
import com.neaterbits.compiler.types.operator.Instantiation;
import com.neaterbits.compiler.types.operator.Logical;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.compiler.types.operator.Relational;
import com.neaterbits.compiler.types.operator.Scope;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.util.ArrayUtils;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaExpressionLexerParser<COMPILATION_UNIT> extends BaseJavaLexerParser<COMPILATION_UNIT> {

    private static final char SEPARATOR = '_';
    
    JavaExpressionLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    private static final JavaToken [] AFTER_NAME_OPERATOR_TOKENS = new JavaToken [] {
            JavaToken.PERIOD,

            JavaToken.ASSIGN,
            
            JavaToken.LPAREN
    };
    
    private static final JavaToken [] EXPRESSION_STATEMENT_OPERATOR_TOKENS = AFTER_NAME_OPERATOR_TOKENS;
    
    private static final JavaToken [] OPERATOR_TOKENS = new JavaToken [] {
            
            JavaToken.INCREMENT,
            JavaToken.DECREMENT,
            
            JavaToken.EQUALS,
            JavaToken.NOT_EQUALS,
            JavaToken.LT,
            JavaToken.GT,
            JavaToken.LTE,
            JavaToken.GTE,
            
            JavaToken.PLUS,
            JavaToken.MINUS,
            JavaToken.MUL,
            JavaToken.DIV,
            JavaToken.MOD,
            
            JavaToken.LOGICAL_AND,
            JavaToken.LOGICAL_OR,
    };

    private static final JavaToken [] IN_EXPRESSION_OPERATOR_TOKENS = ArrayUtils.merge(OPERATOR_TOKENS, AFTER_NAME_OPERATOR_TOKENS);

    final void parseExpressionList() throws IOException, ParserException {

        boolean done = false;
        boolean initial = false;
        
        do {
            final boolean expressionFound = parseExpression();
            
            if (!expressionFound) {
                if (initial) {
                    throw lexer.unexpectedToken();
                }
                else {
                    done = true;
                }
            }
            else {
                
                initial = false;
            }
        } while (!done);
    }
    
    private static final JavaToken [] EXPRESSION_TOKENS = new JavaToken [] {
            
            JavaToken.STRING_LITERAL,
            JavaToken.CHARACTER_LITERAL,
            JavaToken.TRUE,
            JavaToken.FALSE,
            JavaToken.DECIMAL_LITERAL,
            JavaToken.LONG_DECIMAL_LITERAL,
            JavaToken.HEX_LITERAL,
            JavaToken.LONG_HEX_LITERAL,
            JavaToken.OCTAL_LITERAL,
            JavaToken.LONG_OCTAL_LITERAL,
            JavaToken.BINARY_LITERAL,
            JavaToken.LONG_BINARY_LITERAL,
            JavaToken.IDENTIFIER,
    };
            
    final boolean parseExpression() throws IOException, ParserException {

        final boolean expressionFound = parseExpressionToCache();
        
        if (expressionFound) {
            applyAndClearExpressionCache(baseClassExpressionCache);
        }
        
        return expressionFound;
    }

    boolean parseExpressionStatementToCache() throws IOException, ParserException {
        
        return parseExpressionToCache(baseClassExpressionCache, EXPRESSION_STATEMENT_OPERATOR_TOKENS);
    }

    boolean parseExpressionToCache() throws IOException, ParserException {
        
        return parseExpressionToCache(baseClassExpressionCache, IN_EXPRESSION_OPERATOR_TOKENS);
    }

    boolean parseExpressionToCache(ExpressionCache expressionCache) throws IOException, ParserException {

        return parseExpressionToCache(expressionCache, IN_EXPRESSION_OPERATOR_TOKENS);
    }

    private boolean parseExpressionToCache(
            ExpressionCache expressionCache,
            JavaToken [] postfixUnaryOrBinaryOperatorTokens) throws IOException, ParserException {

        OperatorStatus primaryOrUnaryStatus = parsePrimaryOrUnaryOperator(expressionCache);
        
        final boolean expressionFound = primaryOrUnaryStatus != OperatorStatus.NOT_FOUND;
        
        if (expressionFound) {
         
            for (;;) {
                
                if (primaryOrUnaryStatus == OperatorStatus.REQUIRES_PRIMARY) {
                    
                    primaryOrUnaryStatus = parsePrimaryOrUnaryOperator(expressionCache);
                }
                else if (primaryOrUnaryStatus == OperatorStatus.OPTIONAL_OPERATOR) {
                    
                    final OperatorStatus operatorStatus = parseOperatorToCache(expressionCache, postfixUnaryOrBinaryOperatorTokens);
                    
                    if (operatorStatus == OperatorStatus.NOT_FOUND) {
                        break;
                    }
                    else if (operatorStatus == OperatorStatus.REQUIRES_PRIMARY) {
                        primaryOrUnaryStatus = parsePrimaryOrUnaryOperator(expressionCache);
                        
                        if (primaryOrUnaryStatus == OperatorStatus.NOT_FOUND) {
                            
                            if (operatorStatus == OperatorStatus.REQUIRES_PRIMARY) {
                                throw new ParserException("Missing primary");
                            }
                        }
                    }
                    else if (operatorStatus == OperatorStatus.OPTIONAL_OPERATOR) {
                        // Continue
                    }
                    else {
                        throw new UnsupportedOperationException();
                    }
                }
            }
        }
            
        return expressionFound;
    }

    private static final JavaToken [] EXPRESSION_OR_UNARY_OPERATOR_TOKENS = ArrayUtils.merge(
            new JavaToken [] {
                    JavaToken.EXCLAMATION,
                    
                    JavaToken.INCREMENT,
                    JavaToken.DECREMENT,
                    
                    JavaToken.NEW
            },
            EXPRESSION_TOKENS
);

    private OperatorStatus parsePrimaryOrUnaryOperator(ExpressionCache expressionCache) throws IOException, ParserException {
        
        return parsePrimaryOrUnaryOperator(expressionCache, EXPRESSION_OR_UNARY_OPERATOR_TOKENS);
    }
    
    private OperatorStatus parsePrimaryOrUnaryOperator(ExpressionCache expressionCache, JavaToken [] tokens) throws IOException, ParserException {

        final JavaToken token = lexer.lexSkipWS(tokens);
        
        final OperatorStatus status;
        
        switch (token) {
        case IDENTIFIER:
            // Variable or 'this' or method call
            expressionCache.addName(writeCurContext(), getStringRef());
            
            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case DECIMAL_LITERAL:
            expressionCache.addIntegerLiteral(
                    writeCurContext(),
                    tokenizer.asLongWithSeparator(getStringRef(), 0, 0, SEPARATOR),
                    Base.DECIMAL,
                    true,
                    32);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case LONG_DECIMAL_LITERAL:
            expressionCache.addIntegerLiteral(
                    writeCurContext(),
                    tokenizer.asLongWithSeparator(getStringRef(), 0, 1, SEPARATOR),
                    Base.DECIMAL,
                    true,
                    64);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case HEX_LITERAL:
            expressionCache.addIntegerLiteral(
                    writeCurContext(),
                    tokenizer.asHexLongWithSeparator(getStringRef(), 2, 0, SEPARATOR),
                    Base.HEX,
                    true,
                    32);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case LONG_HEX_LITERAL:
            expressionCache.addIntegerLiteral(
                    writeCurContext(),
                    tokenizer.asHexLongWithSeparator(getStringRef(), 2, 1, SEPARATOR),
                    Base.HEX,
                    true,
                    64);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case OCTAL_LITERAL:
            expressionCache.addIntegerLiteral(
                    writeCurContext(),
                    tokenizer.asOctalLongWithSeparator(getStringRef(), 1, 0, SEPARATOR),
                    Base.OCTAL,
                    true,
                    32);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case LONG_OCTAL_LITERAL:
            expressionCache.addIntegerLiteral(
                    writeCurContext(),
                    tokenizer.asOctalLongWithSeparator(getStringRef(), 1, 1, SEPARATOR),
                    Base.OCTAL,
                    true,
                    64);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case BINARY_LITERAL:
            expressionCache.addIntegerLiteral(
                    writeCurContext(),
                    tokenizer.asBinaryLongWithSeparator(getStringRef(), 2, 0, SEPARATOR),
                    Base.BINARY,
                    true,
                    32);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case LONG_BINARY_LITERAL:
            expressionCache.addIntegerLiteral(
                    writeCurContext(),
                    tokenizer.asBinaryLongWithSeparator(getStringRef(), 2, 1, SEPARATOR),
                    Base.BINARY,
                    true,
                    64);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case STRING_LITERAL:
            expressionCache.addStringLiteral(
                    writeCurContext(),
                    lexer.getStringRef(1, 1));
            
            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;
            
        case CHARACTER_LITERAL:
            expressionCache.addCharacterLiteral(
                    writeCurContext(),
                    lexer.getCharacter(1));
            
            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        case TRUE:
            expressionCache.addBooleanLiteral(writeCurContext(), true);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;
            
        case FALSE:
            expressionCache.addBooleanLiteral(writeCurContext(), false);

            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;
            
        case EXCLAMATION:
            expressionCache.addOperator(writeCurContext(), Logical.NOT);

            status = OperatorStatus.REQUIRES_PRIMARY;
            break;
            
        case INCREMENT:
            expressionCache.addOperator(writeCurContext(), IncrementDecrement.PRE_INCREMENT);

            status = OperatorStatus.REQUIRES_PRIMARY;
            break;

        case DECREMENT:
            expressionCache.addOperator(writeCurContext(), IncrementDecrement.PRE_DECREMENT);

            status = OperatorStatus.REQUIRES_PRIMARY;
            break;
            
        case NEW:
            expressionCache.addOperator(writeCurContext(), Instantiation.NEW);
            
            status = OperatorStatus.REQUIRES_PRIMARY;
            break;

        case NONE:
            status = OperatorStatus.NOT_FOUND;
            break;
            
        default:
            throw lexer.unexpectedToken();
        }

        return status;
    }
    
    enum OperatorStatus {
        REQUIRES_PRIMARY,
        OPTIONAL_OPERATOR,
        NOT_FOUND
    }
    
    private OperatorStatus parseOperatorToCache(ExpressionCache expressionCache, JavaToken [] operatorTokens) throws IOException, ParserException {
        
        final JavaToken operatorToken = lexer.lexSkipWS(operatorTokens);
        
        OperatorStatus status = operatorToken != JavaToken.NONE
                ? OperatorStatus.REQUIRES_PRIMARY
                : OperatorStatus.NOT_FOUND;
        
        switch (operatorToken) {
        
        case INCREMENT:
            addOperator(expressionCache, writeCurContext(), IncrementDecrement.POST_INCREMENT);
            
            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;
            
        case DECREMENT:
            addOperator(expressionCache, writeCurContext(), IncrementDecrement.POST_DECREMENT);
            
            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;
        
        case EQUALS:
            addOperator(expressionCache, writeCurContext(), Relational.EQUALS);
            break;
            
        case NOT_EQUALS:
            addOperator(expressionCache, writeCurContext(), Relational.NOT_EQUALS);
            break;
            
        case LT:
            addOperator(expressionCache, writeCurContext(), Relational.LESS_THAN);
            break;
            
        case GT:
            addOperator(expressionCache, writeCurContext(), Relational.GREATER_THAN);
            break;

        case LTE:
            addOperator(expressionCache, writeCurContext(), Relational.LESS_THAN_OR_EQUALS);
            break;
            
        case GTE:
            addOperator(expressionCache, writeCurContext(), Relational.GREATER_THAN_OR_EQUALS);
            break;
            
        case LOGICAL_AND:
            addOperator(expressionCache, writeCurContext(), Logical.AND);
            break;
            
        case LOGICAL_OR:
            addOperator(expressionCache, writeCurContext(), Logical.OR);
            break;
            
        case PLUS:
            addOperator(expressionCache, writeCurContext(), Arithmetic.PLUS);
            break;
            
        case MINUS:
            addOperator(expressionCache, writeCurContext(), Arithmetic.MINUS);
            break;

        case MUL:
            addOperator(expressionCache, writeCurContext(), Arithmetic.MULTIPLY);
            break;

        case DIV:
            addOperator(expressionCache, writeCurContext(), Arithmetic.DIVIDE);
            break;

        case MOD:
            addOperator(expressionCache, writeCurContext(), Arithmetic.MODULUS);
            break;
            
        case PERIOD:
            addOperator(expressionCache, writeCurContext(), Scope.NAMES_SEPARATOR);
            break;
            
        case ASSIGN:
            addOperator(expressionCache, writeCurContext(), Assignment.ASSIGN);
            break;
            
        case LPAREN:
            if (expressionCache.getTypeOfLast() != ParseTreeElement.NAME) {
                throw new ParserException("Expected name");
            }
            
            expressionCache.addMethodInvocationStart(writeCurContext());
            
            parseMethodInvocationParametersToCache();
            
            expressionCache.addMethodInvocationEnd();
            
            status = OperatorStatus.OPTIONAL_OPERATOR;
            break;

        default:
            break;
        }

        return status;
    }

    final void parseConditionInParenthesis() throws IOException, ParserException {
        
        final JavaToken lparen = lexer.lexSkipWS(JavaToken.LPAREN);
        
        if (lparen != JavaToken.LPAREN) {
            throw lexer.unexpectedToken();
        }
        
        parseExpressionList();

        final JavaToken rparen = lexer.lexSkipWS(JavaToken.RPAREN);
        
        if (rparen != JavaToken.RPAREN) {
            throw lexer.unexpectedToken();
        }
    }
    
    private static JavaToken [] AFTER_PARAMETER_TOKEN = new JavaToken [] {
      
            JavaToken.COMMA,
            JavaToken.RPAREN
            
    };
    
    final void parseMethodInvocationParametersToCache() throws IOException, ParserException {
        
        parseMethodInvocationParametersToCache(baseClassExpressionCache);
    }

    private void parseMethodInvocationParametersToCache(ExpressionCache expressionCache) throws IOException, ParserException {
        
        final int startContext = writeCurContext();
        
        expressionCache.addParametersStart(startContext);
        
        // See if there is an initial end of parameters
        final JavaToken endOfParameters = lexer.lexSkipWS(JavaToken.RPAREN);
        
        if (endOfParameters != JavaToken.RPAREN) {
            for (;;) {
                
                // final int parameterStartContext = writeCurContext();

                expressionCache.addParameterStart();

                parseExpressionToCache();
                
                expressionCache.addParameterEnd();
                
                final JavaToken paramToken = lexer.lexSkipWS(AFTER_PARAMETER_TOKEN);
                
                if (paramToken == JavaToken.RPAREN) {
                    expressionCache.addParametersEnd(startContext);
                    break;
                }
                else if (paramToken == JavaToken.COMMA) {
                    // Continue with next
                }
                else {
                    throw lexer.unexpectedToken();
                }
            }
        }
    }

    private void addOperator(ExpressionCache expressionCache, int context, Operator operator) throws IOException, ParserException {

        expressionCache.addOperator(context, operator);
    }
}
