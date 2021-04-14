package com.neaterbits.ide.core.ui.actions.types.source;

import com.neaterbits.compiler.model.common.SourceTokenType;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.core.ui.actions.contexts.source.SourceTokenContext;

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
