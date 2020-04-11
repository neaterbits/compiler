package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
