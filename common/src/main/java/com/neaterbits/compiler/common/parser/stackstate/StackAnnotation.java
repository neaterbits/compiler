package com.neaterbits.compiler.common.parser.stackstate;

import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.PrimarySetter;
import com.neaterbits.compiler.common.parser.StackEntry;

public final class StackAnnotation extends StackEntry implements PrimarySetter {

	public StackAnnotation(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addPrimary(Primary primary) {
		
	}
}
