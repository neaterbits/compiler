package com.neaterbits.compiler.common.model;

public interface ISourceToken {

	SourceTokenType getTokenType();

	long getStartOffset();

	long getLength();
	
	default boolean isPlaceholderToken() {
		return getStartOffset() == -1L;
	}

	String getTokenTypeDebugName();
	
	default String getTokenDebugString() {
		return getTokenTypeDebugName() + " [" + getStartOffset() + ", " + getLength() + "] ";
	}
}
