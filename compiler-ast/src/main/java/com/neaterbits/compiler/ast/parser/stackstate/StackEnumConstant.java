package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.ast.parser.ParametersSetter;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackEnumConstant extends ListStackEntry<ComplexMemberDefinition> implements ParametersSetter {

	private final String name;
	private List<Expression> parameters;

	public StackEnumConstant(ParseLogger parseLogger, String name) {
		super(parseLogger);

		Objects.requireNonNull(name);
		
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Expression> getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(List<Expression> parameters) {

		Objects.requireNonNull(parameters);

		if (this.parameters != null) {
			throw new IllegalArgumentException("Parameters already set");
		}

		this.parameters = parameters;
	}
}
