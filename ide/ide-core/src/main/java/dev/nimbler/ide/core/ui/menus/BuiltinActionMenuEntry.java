package dev.nimbler.ide.core.ui.menus;

import java.util.Objects;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.common.ui.keys.KeyBindings;
import dev.nimbler.ide.common.ui.keys.KeyCombination;
import dev.nimbler.ide.common.ui.menus.MenuItemEntry;
import dev.nimbler.ide.core.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.core.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.core.ui.actions.BuiltinAction;

public final class BuiltinActionMenuEntry
        extends MenuItemEntry<ActionApplicableParameters, ActionExecuteParameters> {

	private final BuiltinAction builtinAction;
	private final KeyCombination keyCombination;
	
	BuiltinActionMenuEntry(BuiltinAction builtinAction, KeyBindings keyBindings) {

		Objects.requireNonNull(builtinAction);
		
		this.builtinAction = builtinAction;
		this.keyCombination = keyBindings.findKeyCombination(builtinAction.getAction());
	}

	public BuiltinAction getAction() {
		return builtinAction;
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return builtinAction.getAction().isApplicableInContexts(parameters, focusedViewContexts, allContexts);
	}

	@Override
	public KeyCombination getKeyCombination() {
		return keyCombination;
	}

	@Override
	public void execute(ActionExecuteParameters parameters) {
		builtinAction.getAction().execute(parameters);
	}

	@Override
	public String getTranslationNamespace() {
		return builtinAction.getTranslationNamespace();
	}

	@Override
	public String getTranslationId() {
		return builtinAction.getTranslationId();
	}

	@Override
	public String toString() {
		return "BuiltinActionMenuEntry [action=" + builtinAction + "]";
	}
}
