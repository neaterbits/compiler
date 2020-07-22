package com.neaterbits.compiler.parser.listener.common;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.operator.IncrementDecrement;
import com.neaterbits.compiler.util.operator.Operator;

public interface InfixParserListener<COMPILATION_UNIT> extends ParserListener<COMPILATION_UNIT> {
    
    void onUnaryExpressionStart(int startContext, Operator operator);
    
    void onUnaryExpressionEnd(int startContext, Context endContext);

	void onExpressionBinaryOperator(int leafContext, Operator operator);
	
	void onIncrementDecrementExpressionStart(int startContext, IncrementDecrement operator);
	
	void onIncrementDecrementExpressionEnd(int startContext, Context endContext);
}
