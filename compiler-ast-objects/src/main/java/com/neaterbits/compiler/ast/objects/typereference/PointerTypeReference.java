package com.neaterbits.compiler.ast.objects.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.type.NamedType;
import com.neaterbits.compiler.ast.objects.type.PointerType;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class PointerTypeReference extends ResolvedTypeReference {

	private final PointerType pointerType;

	public PointerTypeReference(Context context, int typeNo, PointerType pointerType) {
		super(context, typeNo);

		Objects.requireNonNull(pointerType);

		this.pointerType = pointerType;
	}

	@Override
	public String getDebugName() {
		return getTypeName().getName() + "_ptr";
	}

	public PointerType getType() {
		return pointerType;
	}

	@Override
	public TypeName getTypeName() {
		return ((NamedType)pointerType.getDelegate()).getTypeName();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.POINTER_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onPointerTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
