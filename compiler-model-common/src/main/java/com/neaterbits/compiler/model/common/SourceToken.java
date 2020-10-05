package com.neaterbits.compiler.model.common;

import java.util.Objects;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.util.parse.context.Context;

public final class SourceToken implements ISourceToken {

	private final int parseTreeReference;
	private final SourceTokenType tokenType;
	private final long startOffset;
	private final long length;
	private final TypeName typeName;
	private final String astElement;
	private final boolean isPlaceholder;

	public SourceToken(String astElement) {
		this(-1, SourceTokenType.UNKNOWN, -1, 0, null, astElement, true);
	}

	public SourceToken(int parseTreeReference, SourceTokenType tokenType, Context context, TypeName typeName, String astElement) {
		this(parseTreeReference, tokenType, context.getStartOffset(), context.getEndOffset() - context.getStartOffset() + 1, typeName, astElement, false);
	}

	private SourceToken(int parseTreeReference, SourceTokenType tokenType, long startOffset, long length, TypeName typeName, String astElement, boolean isPlaceholder) {

		Objects.requireNonNull(tokenType);
		Objects.requireNonNull(astElement);

		if (startOffset < 0 && !isPlaceholder) {
			throw new IllegalArgumentException();
		}
		
		if (length <= 0 && !isPlaceholder) {
			throw new IllegalArgumentException();
		}
		
		this.parseTreeReference = parseTreeReference;
		this.tokenType = tokenType;
		this.startOffset = startOffset;
		this.length = length;
		this.typeName = typeName;
		this.astElement = astElement;
		this.isPlaceholder = isPlaceholder;
	}

	@Override
	public int getParseTreeReference() {
		return parseTreeReference;
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
