package com.neaterbits.compiler.util.parse.stackstate;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.StackEntry;
import com.neaterbits.compiler.util.parse.stackstate.setters.PrimarySetter;

public final class StackAnnotation<PRIMARY> extends StackEntry implements PrimarySetter<PRIMARY> {

	public StackAnnotation(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public void addPrimary(PRIMARY primary) {
		
	}
}
