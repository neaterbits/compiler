package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.common.log.ParseLogger;

public final class StackClassInstanceCreationExpression extends StackExpressionList {

	private TypeReference type;
	private ConstructorName constructorName;
	
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
}
