package com.neaterbits.compiler.ast.parser.stackstate;

import com.neaterbits.compiler.ast.parser.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
