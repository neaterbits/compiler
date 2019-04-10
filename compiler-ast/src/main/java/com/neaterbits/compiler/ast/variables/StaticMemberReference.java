package com.neaterbits.compiler.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class StaticMemberReference extends VariableReference {

	private final TypeReference classType;
	private final String name;
	private final Context nameContext;

	public StaticMemberReference(Context context, TypeReference type, String name, Context nameContext) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		Objects.requireNonNull(nameContext);
		
		this.classType = type;
		this.name = name;
		this.nameContext = nameContext;
	}
	
	public TypeReference getClassType() {
		return classType;
	}

	public String getName() {
		return name;
	}

	public Context getNameContext() {
		return nameContext;
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
