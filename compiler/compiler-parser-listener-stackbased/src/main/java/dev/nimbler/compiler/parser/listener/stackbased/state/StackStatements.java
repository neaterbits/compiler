package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
