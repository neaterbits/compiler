package dev.nimbler.ide.core.ui.actions.contexts;

import java.util.Collections;
import java.util.List;

import dev.nimbler.ide.common.model.clipboard.ClipboardDataType;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;

public final class ClipboardPasteableContext extends ActionContext {

	private final List<ClipboardDataType> pasteableDataTypes;

	public ClipboardPasteableContext(List<ClipboardDataType> pasteableDataTypes) {
		this.pasteableDataTypes = Collections.unmodifiableList(pasteableDataTypes);
	}

	public List<ClipboardDataType> getPasteableDataTypes() {
		return pasteableDataTypes;
	}
}
