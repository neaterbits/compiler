package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ExpressionSetter;
import com.neaterbits.compiler.common.parser.StackEntry;
import com.neaterbits.compiler.common.parser.TypeReferenceSetter;
import com.neaterbits.compiler.common.parser.VariableModifierSetter;

public final class StackResource extends StackEntry 
		implements VariableModifierSetter, TypeReferenceSetter, VariableNameSetter, ExpressionSetter {

	private final List<VariableModifier> modifiers;
	private TypeReference typeReference;
	private String name;
	private int numDims;
	private Expression initializer;

	public StackResource(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.modifiers = new ArrayList<>();
	}

	@Override
	public void addModifier(VariableModifier modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public List<VariableModifier> getModifiers() {
		return modifiers;
	}

	@Override
	public void setTypeReference(TypeReference typeReference) {
		Objects.requireNonNull(typeReference);
		
		if (this.typeReference != null) {
			throw new IllegalStateException("typeReference already set: " + typeReference);
		}

		this.typeReference = typeReference;
	}

	@Override
	public void init(String name, int numDims) {
		Objects.requireNonNull(name);

		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		
		this.name = name;
		this.numDims = numDims;
	}

	public String getName() {
		return name;
	}

	public int getNumDims() {
		return numDims;
	}

	public TypeReference getTypeReference() {
		return typeReference;
	}
	
	@Override
	public void addExpression(Expression expression) {
		if (initializer != null) {
			throw new IllegalStateException("Initializer already set");
		}
		
		this.initializer = expression;
	}

	public Expression getInitializer() {
		return initializer;
	}
}
