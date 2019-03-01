package com.neaterbits.compiler.common.model;

import java.util.Objects;

public final class SourceToken {

	private final SourceTokenType tokenType;
	private final long startOffset;
	private final long length;
	
	public SourceToken(SourceTokenType tokenType, long startOffset, long length) {

		Objects.requireNonNull(tokenType);
		
		this.tokenType = tokenType;
		this.startOffset = startOffset;
		this.length = length;
	}

	public SourceTokenType getTokenType() {
		return tokenType;
	}

	public long getStartOffset() {
		return startOffset;
	}

	public long getLength() {
		return length;
	}
}
