package com.neaterbits.compiler.common.parser.stackstate;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ParametersSetter;
import com.neaterbits.compiler.common.parser.StackEntry;

public final class StackClassInstanceCreationExpression extends StackEntry implements ParametersSetter {

	private TypeReference type;
	private ConstructorName constructorName;
	private List<Expression> parameters;
	
	public StackClassInstanceCreationExpression(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public TypeReference getType() {
		return type;
	}

	public void setType(TypeReference type) {
		
		Objects.requireNonNull(type);
		
		this.type = type;
	}

	public ConstructorName getConstructorName() {
		return constructorName;
	}

	public void setConstructorName(ConstructorName constructorName) {
		
		Objects.requireNonNull(constructorName);
		
		this.constructorName = constructorName;
	}

	public List<Expression> getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(List<Expression> parameters) {
		
		Objects.requireNonNull(parameters);
		
		if (this.parameters != null) {
			throw new IllegalStateException("Parameters already set");
		}

		this.parameters = parameters;
	}
}
