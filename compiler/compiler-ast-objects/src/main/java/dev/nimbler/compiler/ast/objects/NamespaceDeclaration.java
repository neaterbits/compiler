package dev.nimbler.compiler.ast.objects;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.util.name.NamespaceReference;

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
