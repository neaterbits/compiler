package com.neaterbits.compiler.ast.objects.typereference;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public class LibraryTypeReference extends ResolvedNamedTypeReference {

	public LibraryTypeReference(Context context, int typeNo, TypeName typeName) {
		super(context, typeNo, typeName);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.LIBRARY_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
