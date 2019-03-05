package com.neaterbits.compiler.common.ast;

class BaseASTIterator implements ASTIterator {

	private boolean continueIteration;
	
	BaseASTIterator() {
		this.continueIteration = true;
	}
	
	boolean isContinueIteration() {
		return continueIteration;
	}

	void cancelIteration() {
		
		if (!continueIteration) {
			throw new IllegalStateException();
		}
		
		this.continueIteration = false;
	}

	@Override
	public void onPush(BaseASTElement element) {
		
	}

	@Override
	public boolean onElement(BaseASTElement element) {
		return true;
	}

	@Override
	public boolean onPop(BaseASTElement element) {
		return true;
	}
}
