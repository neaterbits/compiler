package dev.nimbler.compiler.parser.listener.common;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.operator.IncrementDecrement;
import dev.nimbler.compiler.types.operator.Operator;

public interface InfixParseTreeListener<COMPILATION_UNIT> extends ParseTreeListener<COMPILATION_UNIT> {
    
    void onUnaryExpressionStart(int startContext, Operator operator);
    
    void onUnaryExpressionEnd(int startContext, Context endContext);

	void onExpressionBinaryOperator(int leafContext, Operator operator);
	
	void onIncrementDecrementExpressionStart(int startContext, IncrementDecrement operator);
	
	void onIncrementDecrementExpressionEnd(int startContext, Context endContext);
}
