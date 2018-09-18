package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.TypeReferenceSetter;

public final class StackParameterSignature extends BaseStackVariableDeclaration implements TypeReferenceSetter {

	public StackParameterSignature(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
