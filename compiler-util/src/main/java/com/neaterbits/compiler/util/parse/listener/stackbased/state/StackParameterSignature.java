package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.TypeReferenceSetter;

public final class StackParameterSignature<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE>
	extends BaseStackVariableDeclaration<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE>
	implements TypeReferenceSetter<TYPE_REFERENCE> {

	private final boolean varArgs;
	
	public StackParameterSignature(ParseLogger parseLogger, boolean varArgs) {
		super(parseLogger);

		this.varArgs = varArgs;
	}

	public boolean isVarArgs() {
		return varArgs;
	}
}
