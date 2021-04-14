package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackSwitchCaseGroup<SWITCH_CASE_LABEL, STATEMENT>

	extends StackEntry
	implements StatementSetter<STATEMENT> {

	private final List<SWITCH_CASE_LABEL> labels;
	private final List<STATEMENT> statements;
	
	public StackSwitchCaseGroup(ParseLogger parseLogger) {
		super(parseLogger);

		this.labels = new ArrayList<>();
		this.statements = new ArrayList<>();
	}

	public List<SWITCH_CASE_LABEL> getLabels() {
		return labels;
	}

	public void addLabel(SWITCH_CASE_LABEL label) {

		Objects.requireNonNull(label);
		
		labels.add(label);
	}
	
	public List<STATEMENT> getStatements() {
		return statements;
	}

	@Override
	public void addStatement(STATEMENT statement) {
		Objects.requireNonNull(statement);

		statements.add(statement);
	}
}
