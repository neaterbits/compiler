package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.TypeReferenceSetter;

public final class StackParameterSignature extends BaseStackVariableDeclaration implements TypeReferenceSetter {

	private final boolean varArgs;
	
	public StackParameterSignature(ParseLogger parseLogger, boolean varArgs) {
		super(parseLogger);

		this.varArgs = varArgs;
	}

	public boolean isVarArgs() {
		return varArgs;
	}
}
