package com.neaterbits.compiler.common.ast;

public interface ASTIterator {

	void onPush(BaseASTElement element);
	
	void onElement(BaseASTElement element);
	
	void onPop(BaseASTElement element);
}
