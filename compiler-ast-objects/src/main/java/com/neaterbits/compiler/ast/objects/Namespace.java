package com.neaterbits.compiler.ast.objects;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.name.NamespaceReference;
import com.neaterbits.util.parse.context.Context;

public final class Namespace extends CompilationCode {

	private final ASTSingle<Keyword> keyword;
	
	private final ASTSingle<NamespaceDeclaration> namespaceDeclaration;
	
	private final ASTSingle<CompilationCodeLines> lines;
	
	public Namespace(Context context, Keyword keyword, String [] parts, Context partsContext, CompilationCodeLines lines) {
		super(context);

		Objects.requireNonNull(keyword);
		
		this.keyword = makeSingle(keyword);
		
		this.namespaceDeclaration = makeSingle(new NamespaceDeclaration(partsContext, new NamespaceReference(parts)));

		this.lines = makeSingle(lines);
	}

	public Namespace(Context context, Keyword keyword, NamespaceDeclaration namespaceDeclaration, CompilationCodeLines lines) {
		super(context);

		Objects.requireNonNull(keyword);
		
		Objects.requireNonNull(namespaceDeclaration);
		Objects.requireNonNull(lines);
		
		this.keyword = makeSingle(keyword);
		
		this.namespaceDeclaration = makeSingle(namespaceDeclaration);
		this.lines = makeSingle(lines);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.NAMESPACE;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onNamespace(this, param);

	}

	public Keyword getKeyword() {
		return keyword.get();
	}

	public NamespaceDeclaration getNamespaceDeclaration() {
		return namespaceDeclaration.get();
	}

	public final NamespaceReference getReference() {
		return namespaceDeclaration.get().getNamespaceReference();
	}
	
	public final String [] getParts() {
		return namespaceDeclaration.get().getNamespaceReference().getParts();
	}

	public final CompilationCodeLines getLines() {
		return lines.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(keyword, recurseMode, iterator);
		doIterate(namespaceDeclaration, recurseMode, iterator);
		doIterate(lines, recurseMode, iterator);
	}
}
