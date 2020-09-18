package com.neaterbits.compiler.model.common;

@FunctionalInterface
public interface SourceTokenVisitor {

	default void onPush(ISourceToken token) {
		
	}
	
	default void onPop(ISourceToken token) {
		
	}
	
	void onToken(ISourceToken token);
}
