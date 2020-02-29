package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.PrimarySetter;

public final class StackAnnotation<PRIMARY> extends StackEntry implements PrimarySetter<PRIMARY> {

	public StackAnnotation(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addPrimary(PRIMARY primary) {
		
	}
}
