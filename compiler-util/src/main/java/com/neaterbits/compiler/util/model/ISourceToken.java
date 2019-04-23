package com.neaterbits.compiler.util.model;

import com.neaterbits.compiler.util.TypeName;

public interface ISourceToken {

	SourceTokenType getTokenType();

	long getStartOffset();

	long getLength();
	
	int getParseTreeReference();

	TypeName getTypeName();
	
	String getTokenTypeDebugName();
	
	boolean isPlaceholder();
	
	default String getTokenDebugString() {
		return getTokenTypeDebugName() + " [" + getStartOffset() + ", " + getLength() + "] ";
	}
}
