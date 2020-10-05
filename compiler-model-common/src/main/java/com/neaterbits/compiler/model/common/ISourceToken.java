package com.neaterbits.compiler.model.common;

import com.neaterbits.build.types.TypeName;

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
