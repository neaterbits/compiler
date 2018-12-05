package com.neaterbits.compiler.common.resolver.codemap;

final class MethodCallGraph {

	private int [][] calledFrom;
	private int [][] calledTo;
	
	MethodCallGraph() {
	}
	
	void addMethodCall(int calledFrom, int calledTo) {

		this.calledFrom = ArrayAllocation.allocateIntArray(this.calledFrom, calledFrom + 1);
		this.calledTo = ArrayAllocation.allocateIntArray(this.calledTo, calledTo + 1);
		
		ArrayAllocation.addToSubIntArray(this.calledFrom, calledFrom, calledTo, 3);
		ArrayAllocation.addToSubIntArray(this.calledTo, calledTo, calledFrom, 3);
	}
	
	int [] getMethodsCalledFrom(int calledFrom) {
		return this.calledFrom[calledFrom];
	}
	
	int [] getMethodsCalling(int calledTo) {
		return this.calledTo[calledTo];
	}
}
