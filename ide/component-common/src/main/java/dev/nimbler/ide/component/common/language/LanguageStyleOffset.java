package dev.nimbler.ide.component.common.language;

import java.util.Objects;

public final class LanguageStyleOffset {

	private final long start;
	private final long length;
	private final LanguageStyleable styleable;
	
	public LanguageStyleOffset(long start, long length, LanguageStyleable styleable) {

		Objects.requireNonNull(styleable);
		
		this.start = start;
		this.length = length;
		this.styleable = styleable;
	}

	public long getStart() {
		return start;
	}

	public long getLength() {
		return length;
	}

	public LanguageStyleable getStyleable() {
		return styleable;
	}
}
