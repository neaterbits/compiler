package dev.nimbler.compiler.parser.java.recursive;

import dev.nimbler.compiler.parser.recursive.cached.expressions.LanguageOperatorPrecedence;
import dev.nimbler.compiler.types.operator.Arithmetic;
import dev.nimbler.compiler.types.operator.Assignment;
import dev.nimbler.compiler.types.operator.Bitwise;
import dev.nimbler.compiler.types.operator.IncrementDecrement;
import dev.nimbler.compiler.types.operator.Instantiation;
import dev.nimbler.compiler.types.operator.Logical;
import dev.nimbler.compiler.types.operator.Operator;
import dev.nimbler.compiler.types.operator.Relational;
import dev.nimbler.compiler.types.operator.Scope;

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
