package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.setters.StatementSetter;

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
