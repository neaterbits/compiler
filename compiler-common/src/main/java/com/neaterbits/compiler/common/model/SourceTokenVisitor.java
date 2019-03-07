package com.neaterbits.compiler.common.model;

@FunctionalInterface
public interface SourceTokenVisitor {

	default void onPush(ISourceToken token) {
		
	}
	
	default void onPop(ISourceToken token) {
		
	}
	
	void onToken(ISourceToken token);
}
