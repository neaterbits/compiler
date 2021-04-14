package com.neaterbits.ide.common.ui.view;

import java.util.Collection;

import com.neaterbits.ide.common.model.clipboard.ClipboardDataType;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;

public interface View {

	Collection<ActionContext> getActiveActionContexts();

	void addActionContextListener(ActionContextListener listener);
	
	default void cut() {
		throw new UnsupportedOperationException();
	}
	
	default void copy() {
		throw new UnsupportedOperationException();
	}
	
	default void paste() {
		throw new UnsupportedOperationException();
	}
	
	default Collection<ClipboardDataType> getSupportedPasteDataTypes() {
		return null;
	}
}
