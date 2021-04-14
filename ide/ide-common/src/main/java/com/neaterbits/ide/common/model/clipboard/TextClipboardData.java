package com.neaterbits.ide.common.model.clipboard;

import java.util.Objects;

public final class TextClipboardData extends ClipboardData {

	private final String text;
	
	public TextClipboardData(String text) {

		Objects.requireNonNull(text);
		
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public ClipboardDataType getDataType() {
		return ClipboardDataType.TEXT;
	}
}
