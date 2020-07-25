package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackReturnType<TYPE_REFERENCE> extends StackTypeReferenceSetter<TYPE_REFERENCE> {

	public StackReturnType(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
