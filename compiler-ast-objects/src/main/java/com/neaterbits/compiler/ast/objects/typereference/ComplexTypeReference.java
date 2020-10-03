package com.neaterbits.compiler.ast.objects.typereference;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class ComplexTypeReference extends ResolvedNamedTypeReference {

	public ComplexTypeReference(Context context, int typeNo, TypeName typeName) {
		super(context, typeNo, typeName);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.COMPLEX_TYPE_REFERENCE;
	}

	@Override
    public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onComplexTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
