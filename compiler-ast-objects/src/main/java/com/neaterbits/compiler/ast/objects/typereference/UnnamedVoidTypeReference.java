package com.neaterbits.compiler.ast.objects.typereference;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class UnnamedVoidTypeReference extends TypeReference {

	public UnnamedVoidTypeReference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDebugName() {
		return "unnamed_void";
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.UNNAMED_VOID_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onUnnamedVoid(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
