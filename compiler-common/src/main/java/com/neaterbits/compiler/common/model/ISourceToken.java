package com.neaterbits.compiler.common.model;

import com.neaterbits.compiler.common.TypeName;

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
