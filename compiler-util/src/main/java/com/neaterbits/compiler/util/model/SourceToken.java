package com.neaterbits.compiler.util.model;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class SourceToken implements ISourceToken {

	private final SourceTokenType tokenType;
	private final long startOffset;
	private final long length;
	private final TypeName typeName;
	private final String astElement;
	private final boolean isPlaceholder;

	public SourceToken(String astElement) {
		this(SourceTokenType.UNKNOWN, -1, 0, null, astElement, true);
	}

	public SourceToken(SourceTokenType tokenType, Context context, TypeName typeName, String astElement) {
		this(tokenType, context.getStartOffset(), context.getEndOffset() - context.getStartOffset() + 1, typeName, astElement, false);
	}

	public SourceToken(SourceTokenType tokenType, long startOffset, long length, TypeName typeName, String astElement, boolean isPlaceholder) {

		Objects.requireNonNull(tokenType);
		Objects.requireNonNull(astElement);

		if (startOffset < 0 && !isPlaceholder) {
			throw new IllegalArgumentException();
		}
		
		if (length <= 0 && !isPlaceholder) {
			throw new IllegalArgumentException();
		}
		
		this.tokenType = tokenType;
		this.startOffset = startOffset;
		this.length = length;
		this.typeName = typeName;
		this.astElement = astElement;
		this.isPlaceholder = isPlaceholder;
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
	public TypeName getTypeName() {
		return typeName;
	}

	@Override
	public String getTokenTypeDebugName() {
		return astElement;
	}

	@Override
	public boolean isPlaceholder() {
		return isPlaceholder;
	}

	@Override
	public String toString() {
		return "SourceToken [tokenType=" + tokenType + ", startOffset=" + startOffset + ", length=" + length
				+ ", astElement=" + astElement + "]";
	}
}
