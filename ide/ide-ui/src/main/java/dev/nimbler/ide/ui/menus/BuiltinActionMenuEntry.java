package dev.nimbler.ide.ui.menus;


import dev.nimbler.ide.common.ui.actions.Action;
import dev.nimbler.ide.common.ui.keys.KeyBindings;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.actions.BuiltinAction;

public final class BuiltinActionMenuEntry
        extends BaseActionMenuEntry<
                        ActionApplicableParameters,
                        ActionExecuteParameters,
                        Action<ActionApplicableParameters, ActionExecuteParameters>> {

	private final BuiltinAction builtinAction;
	
	BuiltinActionMenuEntry(BuiltinAction builtinAction, KeyBindings keyBindings) {
	    
	    super(builtinAction.getAction(), keyBindings);

		this.builtinAction = builtinAction;
	}

	public BuiltinAction getAction() {
		return builtinAction;
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
