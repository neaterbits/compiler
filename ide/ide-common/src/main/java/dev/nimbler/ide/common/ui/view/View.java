package dev.nimbler.ide.common.ui.view;

import java.util.Collection;

import dev.nimbler.ide.common.model.clipboard.ClipboardDataType;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;

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
