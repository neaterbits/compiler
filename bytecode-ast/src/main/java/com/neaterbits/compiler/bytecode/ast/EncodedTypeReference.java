package com.neaterbits.compiler.bytecode.ast;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReferenceVisitor;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class EncodedTypeReference extends TypeReference {

	private final String typeName;

	public EncodedTypeReference(Context context, String typeName) {
		super(context);
	
		Objects.requireNonNull(typeName);
		
		this.typeName = typeName;
	}

	public String getEncodedTypeName() {
		return typeName;
	}

	/*
	@Override
	public BaseType getType() {
		throw new UnsupportedOperationException();
	}
	*/

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDebugName() {
		return typeName;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ENCODED_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
