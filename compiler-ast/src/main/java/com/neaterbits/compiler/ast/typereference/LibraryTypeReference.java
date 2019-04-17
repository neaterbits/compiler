package com.neaterbits.compiler.ast.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public class LibraryTypeReference extends ResolvedTypeReference {

	private final TypeName typeName;
	
	public LibraryTypeReference(Context context, TypeName typeName) {
		super(context);

		Objects.requireNonNull(typeName);
		
		this.typeName = typeName;
	}

	@Override
	public BaseType getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName getTypeName() {
		return typeName;
	}

	@Override
	public String getDebugName() {
		return typeName.getName();
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
