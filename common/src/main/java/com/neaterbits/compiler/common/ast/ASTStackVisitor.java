package com.neaterbits.compiler.common.ast;

import com.neaterbits.compiler.common.StackView;

public interface ASTStackVisitor {

	void onElement(BaseASTElement element, StackView<BaseASTElement> stack);
	
}
