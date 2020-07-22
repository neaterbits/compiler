package com.neaterbits.compiler.parser.java;

import com.neaterbits.compiler.parser.recursive.cached.expressions.LanguageOperatorPrecedence;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Assignment;
import com.neaterbits.compiler.util.operator.Bitwise;
import com.neaterbits.compiler.util.operator.IncrementDecrement;
import com.neaterbits.compiler.util.operator.Instantiation;
import com.neaterbits.compiler.util.operator.Logical;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.operator.Scope;

public final class JavaLanguageOperatorPrecedence implements LanguageOperatorPrecedence {

    public static final LanguageOperatorPrecedence INSTANCE = new JavaLanguageOperatorPrecedence();
    
    private JavaLanguageOperatorPrecedence() {

    }

    @Override
    public int getPrecedence(Operator operator) {

        final int precedence;
        
        switch (operator.getOperatorType()) {
        case ARITHMETIC:
            switch ((Arithmetic)operator) {
            
            case PLUS:
            case MINUS:
                precedence = 11;
                break;
                
            case MULTIPLY:
            case DIVIDE:
            case MODULUS:
                precedence = 12;
                break;
            
            case INCREMENT:
            case DECREMENT:
                throw new UnsupportedOperationException();
                
            default:
                throw new UnsupportedOperationException();
            }
            break;
            
        case INCREMENT_DECREMENT:
            
            switch ((IncrementDecrement)operator) {
            
            case PRE_INCREMENT:
            case PRE_DECREMENT:
                precedence = 14;
                break;
                
            case POST_INCREMENT:
            case POST_DECREMENT:
                precedence = 15;
                break;
                
            default:
                throw new UnsupportedOperationException();
            }
            break;
            
        case BITWISE:
            switch ((Bitwise)operator) {
            case AND:
                precedence = 7;
                break;

            case OR:
                precedence = 5;
                break;
                
            case XOR:
                precedence = 6;
                break;
                
            case LEFTSHIFT:
            case RIGHTSHIFT_SIGNED:
            case RIGHTSHIFT_UNSIGNED:
                precedence = 10;
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;
            
        case LOGICAL:
            switch ((Logical)operator) {
            case AND:
                precedence = 4;
                break;
                
            case OR:
                precedence = 3;
                break;

            case NOT:
                precedence = 14;
                break;
                
            default:
                throw new UnsupportedOperationException();
            }
            break;
            
        case RELATIONAL:
            switch ((Relational)operator) {
            case EQUALS:
            case NOT_EQUALS:
                precedence = 8;
                break;

            case LESS_THAN:
            case LESS_THAN_OR_EQUALS:

            case GREATER_THAN:
            case GREATER_THAN_OR_EQUALS:
                precedence = 9;
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;
            
        case INSTANTIATION:
            
            switch ((Instantiation)operator) {
            case NEW:
                precedence = 13;
                break;
                
            default:
                throw new UnsupportedOperationException();
            }
            break;
            
        case SCOPE:
            switch ((Scope)operator) {
            case NAMES_SEPARATOR:
                precedence = 16;
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;
            
        case ASSIGNMENT:
            switch ((Assignment)operator) {
            case ASSIGN:
                precedence = 1;
                break;
                
            default:
                throw new UnsupportedOperationException();
            }
            break;
            
        default:
            throw new UnsupportedOperationException();
        }

        return precedence;
    }
}
