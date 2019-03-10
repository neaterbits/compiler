package com.neaterbits.compiler.ast.parser.stackstate;

import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.parser.PrimarySetter;
import com.neaterbits.compiler.ast.parser.StackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackAnnotation extends StackEntry implements PrimarySetter {

	public StackAnnotation(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addPrimary(Primary primary) {
		
	}
}
