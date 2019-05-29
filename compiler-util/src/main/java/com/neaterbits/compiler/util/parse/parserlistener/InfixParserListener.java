package com.neaterbits.compiler.util.parse.parserlistener;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Notation;
import com.neaterbits.compiler.util.operator.Operator;

public interface InfixParserListener<COMPILATION_UNIT> extends ParserListener<COMPILATION_UNIT> {

	void onExpressionBinaryOperator(Context context, Operator operator);
	
	void onIncrementDecrementExpressionStart(Context context, Arithmetic operator, Notation notation);
	
	void onIncrementDecrementExpressionEnd(Context context);
}
