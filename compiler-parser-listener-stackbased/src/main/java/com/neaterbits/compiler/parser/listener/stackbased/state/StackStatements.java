package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
