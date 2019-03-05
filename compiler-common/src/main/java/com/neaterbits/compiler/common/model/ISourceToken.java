package com.neaterbits.compiler.common.model;

public interface ISourceToken {

	SourceTokenType getTokenType();

	long getStartOffset();

	long getLength();
}
