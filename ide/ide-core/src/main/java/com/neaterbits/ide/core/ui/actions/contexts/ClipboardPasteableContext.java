package com.neaterbits.ide.core.ui.actions.contexts;

import java.util.Collections;
import java.util.List;

import com.neaterbits.ide.common.model.clipboard.ClipboardDataType;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;

public final class ClipboardPasteableContext extends ActionContext {

	private final List<ClipboardDataType> pasteableDataTypes;

	public ClipboardPasteableContext(List<ClipboardDataType> pasteableDataTypes) {
		this.pasteableDataTypes = Collections.unmodifiableList(pasteableDataTypes);
	}

	public List<ClipboardDataType> getPasteableDataTypes() {
		return pasteableDataTypes;
	}
}
