package com.neaterbits.compiler.ast;

import com.neaterbits.compiler.util.StackView;;

public interface ASTStackVisitor<T, STACK extends StackView<T>> {

	void onElement(BaseASTElement element, STACK stack);
	
}
