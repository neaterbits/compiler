package com.neaterbits.compiler.parser.listener.common;

import com.neaterbits.compiler.types.operator.IncrementDecrement;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.util.parse.context.Context;

public interface InfixParseTreeListener<COMPILATION_UNIT> extends ParseTreeListener<COMPILATION_UNIT> {
    
    void onUnaryExpressionStart(int startContext, Operator operator);
    
    void onUnaryExpressionEnd(int startContext, Context endContext);

	void onExpressionBinaryOperator(int leafContext, Operator operator);
	
	void onIncrementDecrementExpressionStart(int startContext, IncrementDecrement operator);
	
	void onIncrementDecrementExpressionEnd(int startContext, Context endContext);
}
