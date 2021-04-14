package com.neaterbits.ide.common.ui.menus;

import com.neaterbits.ide.common.ui.actions.ActionAppParameters;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.common.ui.actions.ActionExeParameters;
import com.neaterbits.ide.common.ui.keys.KeyCombination;

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
