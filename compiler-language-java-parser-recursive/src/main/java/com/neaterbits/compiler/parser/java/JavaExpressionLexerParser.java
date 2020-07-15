package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.parse.FieldAccessType;
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
            JavaToken.MOD
            
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
                
                final JavaToken operatorToken = lexer.lexSkipWS(OPERATOR_TOKENS);
                
                switch (operatorToken) {
                case EQUALS:
                    callListenerAndParseExpression(writeCurContext(), Relational.EQUALS);
                    break;
                    
                case NOT_EQUALS:
                    callListenerAndParseExpression(writeCurContext(), Relational.NOT_EQUALS);
                    break;
                    
                case LT:
                    callListenerAndParseExpression(writeCurContext(), Relational.LESS_THAN);
                    break;
                    
                case GT:
                    callListenerAndParseExpression(writeCurContext(), Relational.GREATER_THAN);
                    break;
    
                case LTE:
                    callListenerAndParseExpression(writeCurContext(), Relational.LESS_THAN_OR_EQUALS);
                    break;
                    
                case GTE:
                    callListenerAndParseExpression(writeCurContext(), Relational.GREATER_THAN_OR_EQUALS);
                    break;
                    
                case PLUS:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.PLUS);
                    break;
                    
                case MINUS:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.MINUS);
                    break;

                case MUL:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.MULTIPLY);
                    break;

                case DIV:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.DIVIDE);
                    break;

                case MOD:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.MODULUS);
                    break;

                default:
                    done = true;
                    break;
                }
            }
        } while (!done);
    }
    
    private void callListenerAndParseExpression(int context, Operator operator) throws IOException, ParserException {
        
        listener.onExpressionBinaryOperator(context, operator);
        
    }

    private static final JavaToken [] EXPRESSION_TOKENS = new JavaToken [] {
            
            JavaToken.IDENTIFIER,
            JavaToken.NUMBER,
    };
            
    final boolean parseExpression() throws IOException, ParserException {
        
        final JavaToken token = lexer.lexSkipWS(EXPRESSION_TOKENS);
        
        boolean expressionFound = true;
        
        switch (token) {
        case IDENTIFIER:
            // Variable or 'this' or method call
            parseVariableReferenceExpression(writeCurContext(), getStringRef());
            break;

        case NUMBER:
            parseNumericLiteral(writeCurContext(), getStringRef());
            break;

        default:
            expressionFound = false;
            break;
        }
        
        return expressionFound;
    }
    
    private void parseVariableReferenceExpression(int context, long stringRef) {
        
        // For now just say this is a variable
        listener.onVariableReference(context, stringRef);
        
    }
    
    private void parseNumericLiteral(int context, long stringRef) {

        listener.onIntegerLiteral(
                context,
                tokenizer.asInt(stringRef),
                Base.DECIMAL,
                true,
                32);
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

    final void parseAnyAdditionalPrimaries() throws IOException, ParserException {

        for (;;) {
            final JavaToken periodToken = lexer.lexSkipWS(JavaToken.PERIOD);
            
            if (periodToken != JavaToken.PERIOD) {
                break;
            }

            parseAnAdditionalPrimary();
        }
    }
    
    private void parseAnAdditionalPrimary() throws IOException, ParserException {
        
        final JavaToken identifierToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);

        if (identifierToken !=  JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        final long identifier = getStringRef();
        final int identifierContext = writeCurContext();
        
        final JavaToken nextToken = lexer.lexSkipWS(JavaToken.LPAREN);
        
        if (nextToken == JavaToken.LPAREN) {
            // Method invocation
            final int methodInvocationContext = writeContext(identifierContext);
            
            listener.onMethodInvocationStart(
                    methodInvocationContext,
                    MethodInvocationType.PRIMARY,
                    null,
                    0,
                    identifier,
                    identifierContext);
            
            parseMethodInvocationParameters();
            
            listener.onMethodInvocationEnd(methodInvocationContext, true, getLexerContext());
        }
        else {
            
            listener.onFieldAccess(
                    identifierContext,
                    FieldAccessType.FIELD,
                    null,
                    null,
                    identifier,
                    identifierContext);
        }
    }
    
    private static JavaToken [] AFTER_PARAMETER_TOKEN = new JavaToken [] {
      
            JavaToken.COMMA,
            JavaToken.RPAREN
            
    };
    
    final void parseMethodInvocationParameters() throws IOException, ParserException {
        
        final int startContext = writeCurContext();
        
        listener.onParametersStart(startContext);
        // See if there is an initial end of parameters
        final JavaToken endOfParameters = lexer.lexSkipWS(JavaToken.RPAREN);
        
        if (endOfParameters == JavaToken.RPAREN) {
            listener.onParametersEnd(startContext, getLexerContext());
        }
        else {
            for (;;) {
                
                final int paramContext = writeCurContext();
                
                listener.onParameterStart(paramContext);
                
                parseExpressionList();
                
                listener.onParameterEnd(paramContext, getLexerContext());

                final JavaToken paramToken = lexer.lexSkipWS(AFTER_PARAMETER_TOKEN);
                
                if (paramToken == JavaToken.RPAREN) {
                    listener.onParametersEnd(startContext, getLexerContext());
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
}
