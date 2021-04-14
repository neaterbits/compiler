package com.neaterbits.ide.common.model.source;

final class SourceElementImpl extends SourceTokenProperties implements SourceElement {

	private final long startOffset;
	private final long length;
	
	SourceElementImpl(
			SourceElementMask flags,
			long startOffset,
			long length) {

		super(flags);
		
		if (startOffset < 0) {
			throw new IllegalArgumentException();
		}
		
		this.startOffset = startOffset;
		this.length = length;
	}


	@Override
	public long getStartOffset() {
		return startOffset;
	}

	@Override
	public long getLength() {
		return length;
	}
}
