package com.neaterbits.compiler.ast;

public interface ASTIterator {

	void onPush(BaseASTElement element);
	
	boolean onElement(BaseASTElement element);
	
	boolean onPop(BaseASTElement element);
}