package com.neaterbits.compiler.util.parse.stackstate;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.stackstate.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.stackstate.setters.StatementSetter;

public class StackStatements<STATEMENT> extends ListStackEntry<STATEMENT> implements StatementSetter<STATEMENT> {

	public StackStatements(ParseLogger parseLogger) {
		super(parseLogger);
	}

	@Override
	public final void addStatement(STATEMENT statement) {
		
		Objects.requireNonNull(statement);
		
		super.add(statement);
	}
}
