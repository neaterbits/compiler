package com.neaterbits.compiler.common.parser;

import java.util.List;

import com.neaterbits.compiler.common.ast.expression.Expression;

public interface ParametersSetter {

	void setParameters(List<Expression> parameters);
	
}
