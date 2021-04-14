package com.neaterbits.ide.swt;

import java.util.Objects;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;

import com.neaterbits.ide.common.model.clipboard.ClipboardData;
import com.neaterbits.ide.common.model.clipboard.ClipboardDataType;
import com.neaterbits.ide.common.model.clipboard.TextClipboardData;
import com.neaterbits.ide.core.ui.view.SystemClipboard;

final class SWTSystemClipboard implements SystemClipboard {

	private final Clipboard clipboard;

	SWTSystemClipboard(Display display) {
		this.clipboard = new Clipboard(display);
	}
	
	@Override
	public ClipboardData getData() {
		
		final String plainText = (String)clipboard.getContents(TextTransfer.getInstance());
		
		return plainText != null ? new TextClipboardData(plainText) : null;
	}

	@Override
	public ClipboardDataType getDataType() {
		return clipboard.getContents(TextTransfer.getInstance()) != null
				? ClipboardDataType.TEXT
				: null;
	}

	@Override
	public boolean hasDataType(ClipboardDataType dataType) {

		Objects.requireNonNull(dataType);
		
		return dataType.equals(getDataType());
	}
}
