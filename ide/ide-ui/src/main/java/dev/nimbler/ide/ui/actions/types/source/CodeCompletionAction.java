package dev.nimbler.ide.ui.actions.types.source;

import dev.nimbler.compiler.model.common.SourceTokenType;
import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.actions.contexts.source.SourceTokenContext;

public class CodeCompletionAction extends SourceAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedContexts,
			ActionContexts allContexts) {

		final SourceTokenContext sourceTokenContext = focusedContexts.getOfType(SourceTokenContext.class);

		final boolean isApplicable;
		
		if (sourceTokenContext == null) {
			isApplicable = false;
		}
		else {
			final SourceTokenType tokenType = sourceTokenContext.getTokenType();
			
			isApplicable = tokenType.isTypeName() || tokenType.isVariable();
		}
		
		return isApplicable;
	}
}
