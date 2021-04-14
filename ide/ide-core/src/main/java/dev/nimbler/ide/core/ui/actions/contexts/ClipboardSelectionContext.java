package dev.nimbler.ide.core.ui.actions.contexts;

import java.util.Objects;

import dev.nimbler.ide.common.model.clipboard.ClipboardDataType;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;

// Anything that is selectable
public final class ClipboardSelectionContext extends ActionContext {
	
	private final ClipboardDataType clipboardDataType;

	public ClipboardSelectionContext(ClipboardDataType clipboardDataType) {

		Objects.requireNonNull(clipboardDataType);
		
		this.clipboardDataType = clipboardDataType;
	}

	public ClipboardDataType getClipboardDataType() {
		return clipboardDataType;
	}
}
