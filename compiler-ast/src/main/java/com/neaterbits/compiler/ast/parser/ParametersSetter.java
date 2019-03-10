package com.neaterbits.compiler.ast.parser;

import java.util.List;

import com.neaterbits.compiler.ast.expression.Expression;

public interface ParametersSetter {

	void setParameters(List<Expression> parameters);
	
}
