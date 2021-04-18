package dev.nimbler.compiler.parser.listener.common;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.types.operator.IncrementDecrement;
import dev.nimbler.compiler.types.operator.Operator;

public class InfixParseTreeAdapter<COMPILATION_UNIT>
    extends ParseTreeAdapter<COMPILATION_UNIT>
    implements InfixParseTreeListener<COMPILATION_UNIT> {

    @Override
    public void onUnaryExpressionStart(int startContext, Operator operator) {
        
    }

    @Override
    public void onUnaryExpressionEnd(int startContext, Context endContext) {
        
    }

    @Override
    public void onExpressionBinaryOperator(int leafContext, Operator operator) {
        
    }

    @Override
    public void onIncrementDecrementExpressionStart(int startContext, IncrementDecrement operator) {
        
    }

    @Override
    public void onIncrementDecrementExpressionEnd(int startContext, Context endContext) {
        
    }
}
