package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.type.BaseType;

public final class StaticMemberReference extends VariableReference {

	private final TypeReference classType;
	private final String name;

	public StaticMemberReference(Context context, TypeReference type, String name) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		
		this.classType = type;
		this.name = name;
	}
	
	public TypeReference getClassType() {
		return classType;
	}

	public String getName() {
		return name;
	}

	@Override
	public BaseType getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onStaticMemberReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}

}
