package com.neaterbits.compiler.common.ast;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class Namespace extends CompilationCode {

	private final NamespaceReference namespaceReference;
	
	private final ASTSingle<CompilationCodeLines> lines;
	
	public Namespace(Context context, String name, String [] parts, CompilationCodeLines lines) {
		super(context);

		this.namespaceReference = new NamespaceReference(name, parts);

		this.lines = makeSingle(lines);
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onNamespace(this, param);

	}

	public final String getName() {
		return namespaceReference.getName();
	}

	public final String [] getParts() {
		return namespaceReference.getParts();
	}

	public final CompilationCodeLines getLines() {
		return lines.get();
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(lines, recurseMode, visitor);
	}
}
