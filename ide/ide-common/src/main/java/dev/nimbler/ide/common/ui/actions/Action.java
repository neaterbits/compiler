package dev.nimbler.ide.common.ui.actions;

public abstract class Action<
            APPLICABLE_PARAMETERS extends ActionAppParameters,
            EXECUTE_PARAMETERS extends ActionExeParameters> {

	public abstract void execute(EXECUTE_PARAMETERS parameters) throws ActionExecutionException;
	
	public abstract boolean isApplicableInContexts(
	        APPLICABLE_PARAMETERS parameters,
	        ActionContexts focusedContexts,
	        ActionContexts allContexts);
	
}
