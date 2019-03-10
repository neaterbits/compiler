package com.neaterbits.compiler.ast;

import java.util.Objects;

import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class Namespace extends CompilationCode {

	private final NamespaceReference namespaceReference;
	
	private final ASTSingle<CompilationCodeLines> lines;
	
	public Namespace(Context context, String [] parts, CompilationCodeLines lines) {
		super(context);

		this.namespaceReference = new NamespaceReference(parts);

		this.lines = makeSingle(lines);
	}

	public Namespace(Context context, NamespaceReference namespaceReference, CompilationCodeLines lines) {
		super(context);

		Objects.requireNonNull(namespaceReference);
		Objects.requireNonNull(lines);
		
		this.namespaceReference = namespaceReference;
		this.lines = makeSingle(lines);
	}


	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onNamespace(this, param);

	}

	public final NamespaceReference getReference() {
		return namespaceReference;
	}
	
	public final String [] getParts() {
		return namespaceReference.getParts();
	}

	public final CompilationCodeLines getLines() {
		return lines.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(lines, recurseMode, iterator);
	}
}
