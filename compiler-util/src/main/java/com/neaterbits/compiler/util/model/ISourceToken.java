package com.neaterbits.compiler.util.model;

import com.neaterbits.compiler.util.TypeName;

public interface ISourceToken {

	SourceTokenType getTokenType();

	long getStartOffset();

	long getLength();

	TypeName getTypeName();
	
	String getTokenTypeDebugName();
	
	default String getTokenDebugString() {
		return getTokenTypeDebugName() + " [" + getStartOffset() + ", " + getLength() + "] ";
	}
}
