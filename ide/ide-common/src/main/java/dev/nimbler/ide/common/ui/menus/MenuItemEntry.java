package dev.nimbler.ide.common.ui.menus;

import dev.nimbler.ide.common.ui.actions.ActionAppParameters;
import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.common.ui.actions.ActionExeParameters;
import dev.nimbler.ide.common.ui.keys.KeyCombination;

public abstract class MenuItemEntry<
            APPLICABLE_PARAMETERS extends ActionAppParameters,
            EXECUTE_PARAMETERS extends ActionExeParameters> extends TextMenuEntry {

	public abstract boolean isApplicableInContexts(
			APPLICABLE_PARAMETERS parameters,
			ActionContexts focusedViewContexts,
			ActionContexts allContexts);
	
	public abstract void execute(EXECUTE_PARAMETERS parameters);
	
	public abstract KeyCombination getKeyCombination();
}
