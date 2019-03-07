package com.neaterbits.compiler.common.model;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.BaseASTElement;

public final class SourceToken implements ISourceToken {

	private final SourceTokenType tokenType;
	private final long startOffset;
	private final long length;
	private final BaseASTElement astElement;
	
	public SourceToken(SourceTokenType tokenType, long startOffset, long length, BaseASTElement astElement) {

		Objects.requireNonNull(tokenType);
		Objects.requireNonNull(astElement);

		if (startOffset < 0) {
			throw new IllegalArgumentException();
		}
		
		if (length <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.tokenType = tokenType;
		this.startOffset = startOffset;
		this.length = length;
		this.astElement = astElement;
	}

	@Override
	public SourceTokenType getTokenType() {
		return tokenType;
	}

	@Override
	public long getStartOffset() {
		return startOffset;
	}

	@Override
	public long getLength() {
		return length;
	}
	
	@Override
	public String getTokenTypeDebugName() {
		return astElement.getClass().getSimpleName();
	}

	BaseASTElement getAstElement() {
		return astElement;
	}

	@Override
	public String toString() {
		return "SourceToken [tokenType=" + tokenType + ", startOffset=" + startOffset + ", length=" + length
				+ ", astElement=" + astElement + "]";
	}
}
