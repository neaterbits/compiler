package com.neaterbits.compiler.ast.objects;

import java.util.Objects;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.name.NamespaceReference;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.NAMESPACE_DECLARATION;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
