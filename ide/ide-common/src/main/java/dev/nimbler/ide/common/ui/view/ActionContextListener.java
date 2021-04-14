package dev.nimbler.ide.common.ui.view;

import java.util.Collection;

import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;

public interface ActionContextListener {

	void onUpdated(Collection<ActionContext> updatedContexts);
	
}
