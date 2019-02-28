package com.neaterbits.compiler.common.ast;

import com.neaterbits.compiler.common.StackView;

public interface ASTStackVisitor<T, STACK extends StackView<T>> {

	void onElement(BaseASTElement element, STACK stack);
	
}
