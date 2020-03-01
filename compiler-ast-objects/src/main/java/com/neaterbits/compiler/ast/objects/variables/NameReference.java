package com.neaterbits.compiler.ast.objects.variables;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class NameReference extends VariableReference {

	private final String name;

	public NameReference(Context context, String name) {
		super(context);

		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public TypeReference getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.NAME_REFERENCE;
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onNameReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
