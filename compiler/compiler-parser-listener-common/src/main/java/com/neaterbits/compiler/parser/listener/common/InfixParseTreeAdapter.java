package com.neaterbits.compiler.parser.listener.common;

import com.neaterbits.compiler.types.operator.IncrementDecrement;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.util.parse.context.Context;

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
