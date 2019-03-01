package com.neaterbits.compiler.common.ast;

class BaseASTIterator implements ASTIterator {

	@Override
	public void onPush(BaseASTElement element) {
		
	}

	@Override
	public boolean onElement(BaseASTElement element) {
		return true;
	}

	@Override
	public void onPop(BaseASTElement element) {
		
	}
}
