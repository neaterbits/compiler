package dev.nimbler.ide.common.ui.actions;

import java.util.Collection;

import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;

public interface ActionContexts {

	<T extends ActionContext> Collection<T> getListOfType(Class<T> cl);

	<T extends ActionContext> T getOfType(Class<T> cl);
	
	boolean hasOfType(Class<?> cl);

}
