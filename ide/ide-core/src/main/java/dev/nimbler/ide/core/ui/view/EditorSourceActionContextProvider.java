package dev.nimbler.ide.core.ui.view;

import java.util.Collection;

import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;

public interface EditorSourceActionContextProvider {

	Collection<ActionContext> getActionContexts(long cursorOffset);
	
}
