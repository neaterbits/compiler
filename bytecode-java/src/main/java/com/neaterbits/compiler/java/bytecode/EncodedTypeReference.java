package com.neaterbits.compiler.java.bytecode;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.TypeReferenceVisitor;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.type.BaseType;

public final class EncodedTypeReference extends TypeReference {

	private final String typeName;

	public EncodedTypeReference(Context context, String typeName) {
		super(context);
	
		Objects.requireNonNull(typeName);
		
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	@Override
	public BaseType getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDebugName() {
		return typeName;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
