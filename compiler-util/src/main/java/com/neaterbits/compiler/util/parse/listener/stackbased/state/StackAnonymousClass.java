package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackAnonymousClass<
		COMPLEX_MEMBER_DEFINITION,
		CONSTRUCTOR_MEMBER extends COMPLEX_MEMBER_DEFINITION,
		CLASS_METHOD_MEMBER extends COMPLEX_MEMBER_DEFINITION>

	extends StackClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER> {

	public StackAnonymousClass(ParseLogger parseLogger) {
		super(parseLogger);
	}
}
