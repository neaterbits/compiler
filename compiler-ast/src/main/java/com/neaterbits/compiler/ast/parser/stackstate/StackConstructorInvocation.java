package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.block.ConstructorInvocation;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.parser.ParametersSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackConstructorInvocation extends StackExpressionList implements ParametersSetter {

	private final ConstructorInvocation type;
	private List<Expression> parameters;
	
	public StackConstructorInvocation(ParseLogger parseLogger, ConstructorInvocation type) {
		super(parseLogger);
		
		Objects.requireNonNull(type);
		
		this.type = type;
	}

	public ConstructorInvocation getType() {
		return type;
	}

	@Override
	public void setParameters(List<Expression> parameters) {

		Objects.requireNonNull(parameters);
		
		if (this.parameters != null) {
			throw new IllegalStateException("Parameters already set");
		}
		
		this.parameters = parameters;
	}

	public List<Expression> getParameters() {
		return parameters;
	}
}
