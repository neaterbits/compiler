package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.VariableModifierSetter;

public abstract class BaseStackVariableDeclaration<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE> extends StackEntry
	implements VariableModifierSetter<VARIABLE_MODIFIER_HOLDER>, TypeReferenceSetter<TYPE_REFERENCE>, VariableNameSetter {
	private final List<VARIABLE_MODIFIER_HOLDER> modifiers;
	private TYPE_REFERENCE typeReference;
	private String name;
	private Context nameContext;
	private int numDims;

	BaseStackVariableDeclaration(ParseLogger parseLogger) {
		super(parseLogger);
	
		this.modifiers = new ArrayList<>();
	}
	
	@Override
	public final void addModifier(VARIABLE_MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public final List<VARIABLE_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}

	@Override
	public final void setTypeReference(TYPE_REFERENCE typeReference) {
		Objects.requireNonNull(typeReference);
		
		if (this.typeReference != null) {
			throw new IllegalStateException("typeReference already set: " + typeReference);
		}

		this.typeReference = typeReference;
	}

	@Override
	public final void init(String name, Context nameContext, int numDims) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(nameContext);

		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		
		this.name = name;
		this.nameContext = nameContext;
		this.numDims = numDims;
	}
	
	public final String getName() {
		return name;
	}
	
	public final Context getNameContext() {
		return nameContext;
	}

	public final int getNumDims() {
		return numDims;
	}

	public final TYPE_REFERENCE getTypeReference() {
		return typeReference;
	}
}
