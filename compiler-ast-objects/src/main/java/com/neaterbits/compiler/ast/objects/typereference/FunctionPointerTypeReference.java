package com.neaterbits.compiler.ast.objects.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.type.FunctionPointerType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class FunctionPointerTypeReference extends ResolvedTypeReference {

	private final FunctionPointerType type;
	
	public FunctionPointerTypeReference(Context context, FunctionPointerType type) {
		super(context);

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	public FunctionPointerType getType() {
		return type;
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDebugName() {
		return type.toString();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FUNCTION_POINTER_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onFunctionPointerTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
