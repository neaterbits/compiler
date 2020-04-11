package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackAnnotation<PRIMARY> extends StackEntry implements PrimarySetter<PRIMARY> {

	public StackAnnotation(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addPrimary(PRIMARY primary) {
		
	}
}
