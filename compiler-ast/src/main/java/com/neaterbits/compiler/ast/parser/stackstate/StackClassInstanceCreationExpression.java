package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.parser.ClassMethodMemberSetter;
import com.neaterbits.compiler.ast.parser.ParametersSetter;
import com.neaterbits.compiler.ast.parser.StackEntry;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackClassInstanceCreationExpression extends StackEntry implements ParametersSetter, ClassMethodMemberSetter {

	private TypeReference type;
	private ConstructorName constructorName;
	private List<Expression> parameters;
	
	// Anonymous classes
	private final List<ClassMethodMember> methods;
	
	public StackClassInstanceCreationExpression(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.methods = new ArrayList<>();
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

	public List<ClassMethodMember> getMethods() {
		return methods;
	}

	@Override
	public void addMethod(ClassMethodMember method) {
		Objects.requireNonNull(method);

		methods.add(method);
	}
}
