package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Assignment;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.operator.Scope;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaExpressionLexerParser<COMPILATION_UNIT> extends BaseJavaLexerParser<COMPILATION_UNIT> {

    JavaExpressionLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    private static final JavaToken [] AFTER_NAME_OPERATOR_TOKENS = new JavaToken [] {
            JavaToken.PERIOD,

            JavaToken.ASSIGN,
            
            JavaToken.LPAREN
    };
    
    private static final JavaToken [] EXPRESSION_STATEMENT_OPERATOR_TOKENS = AFTER_NAME_OPERATOR_TOKENS;
    
    private static final JavaToken [] OPERATOR_TOKENS = new JavaToken [] {
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
    };
    
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
            
            JavaToken.IDENTIFIER,
            JavaToken.NUMBER,
    };
            
    final boolean parseExpression() throws IOException, ParserException {

        final boolean expressionFound = parseExpressionToCache();
        
        if (expressionFound) {
            applyAndClearExpressionCache();
        }
        
        return expressionFound;
    }

    boolean parseExpressionStatementToCache() throws IOException, ParserException {
        
        return parseExpressionToCache(EXPRESSION_STATEMENT_OPERATOR_TOKENS);
    }

    boolean parseExpressionToCache() throws IOException, ParserException {
        
        return parseExpressionToCache(OPERATOR_TOKENS);
    }

    private boolean parseExpressionToCache(JavaToken [] operatorTokens) throws IOException, ParserException {

        final boolean expressionFound = parsePrimary();
        
        if (expressionFound) {
         
            for (;;) {

                final OperatorStatus status = parseOperatorToCache(operatorTokens);
                
                if (status == OperatorStatus.NOT_FOUND) {
                    break;
                }
                else if (status == OperatorStatus.REQUIRES_PRIMARY) {
                    if (!parsePrimary()) {
                        if (status == OperatorStatus.REQUIRES_PRIMARY) {
                            throw new ParserException("Missing primary");
                        }
                    }
                }
                else if (status == OperatorStatus.OPTIONAL_OPERATOR) {
                    // Continue
                }
                else {
                    throw new UnsupportedOperationException();
                }
            }
        }
            
        return expressionFound;
    }

    private boolean parsePrimary() throws IOException, ParserException {
        
        final JavaToken token = lexer.lexSkipWS(EXPRESSION_TOKENS);
        
        switch (token) {
        case IDENTIFIER:
            // Variable or 'this' or method call
            expressionCache.addName(writeCurContext(), getStringRef());
            break;

        case NUMBER:
            expressionCache.addIntegerLiteral(
                    writeCurContext(),
                    tokenizer.asInt(getStringRef()),
                    Base.DECIMAL,
                    true,
                    32);
            break;
            
        case NONE:
            break;
            
        default:
            throw lexer.unexpectedToken();
        }

        return token != JavaToken.NONE;
    }
    
    enum OperatorStatus {
        REQUIRES_PRIMARY,
        OPTIONAL_OPERATOR,
        NOT_FOUND
    }
    
    private OperatorStatus parseOperatorToCache(JavaToken [] operatorTokens) throws IOException, ParserException {
        
        final JavaToken operatorToken = lexer.lexSkipWS(operatorTokens);
        
        OperatorStatus status = operatorToken != JavaToken.NONE
                ? OperatorStatus.REQUIRES_PRIMARY
                : OperatorStatus.NOT_FOUND;
        
        switch (operatorToken) {
        case EQUALS:
            addOperator(writeCurContext(), Relational.EQUALS);
            break;
            
        case NOT_EQUALS:
            addOperator(writeCurContext(), Relational.NOT_EQUALS);
            break;
            
        case LT:
            addOperator(writeCurContext(), Relational.LESS_THAN);
            break;
            
        case GT:
            addOperator(writeCurContext(), Relational.GREATER_THAN);
            break;

        case LTE:
            addOperator(writeCurContext(), Relational.LESS_THAN_OR_EQUALS);
            break;
            
        case GTE:
            addOperator(writeCurContext(), Relational.GREATER_THAN_OR_EQUALS);
            break;
            
        case PLUS:
            addOperator(writeCurContext(), Arithmetic.PLUS);
            break;
            
        case MINUS:
            addOperator(writeCurContext(), Arithmetic.MINUS);
            break;

        case MUL:
            addOperator(writeCurContext(), Arithmetic.MULTIPLY);
            break;

        case DIV:
            addOperator(writeCurContext(), Arithmetic.DIVIDE);
            break;

        case MOD:
            addOperator(writeCurContext(), Arithmetic.MODULUS);
            break;
            
        case PERIOD:
            addOperator(writeCurContext(), Scope.NAMES_SEPARATOR);
            break;
            
        case ASSIGN:
            addOperator(writeCurContext(), Assignment.ASSIGN);
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

    private void addOperator(int context, Operator operator) throws IOException, ParserException {

        expressionCache.addOperator(context, operator);
    }
}
