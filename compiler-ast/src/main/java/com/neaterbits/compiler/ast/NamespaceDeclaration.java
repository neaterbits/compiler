package com.neaterbits.compiler.ast;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;

public final class NamespaceDeclaration extends BaseASTElement {

	private final NamespaceReference namespaceReference;

	public NamespaceDeclaration(Context context, NamespaceReference namespaceReference) {
		super(context);

		Objects.requireNonNull(namespaceReference);
		
		this.namespaceReference = namespaceReference;
	}

	public NamespaceReference getNamespaceReference() {
		return namespaceReference;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
