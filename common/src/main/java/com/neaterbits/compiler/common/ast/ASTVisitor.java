package com.neaterbits.compiler.common.ast;

public interface ASTVisitor {
	void onElement(BaseASTElement element);
}
