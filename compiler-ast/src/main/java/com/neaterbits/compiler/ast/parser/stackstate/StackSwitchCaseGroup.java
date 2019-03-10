package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.parser.StackEntry;
import com.neaterbits.compiler.ast.parser.StatementSetter;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.ast.statement.SwitchCaseLabel;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackSwitchCaseGroup extends StackEntry implements StatementSetter {

	private final List<SwitchCaseLabel> labels;
	private final List<Statement> statements;
	
	public StackSwitchCaseGroup(ParseLogger parseLogger) {
		super(parseLogger);

		this.labels = new ArrayList<>();
		this.statements = new ArrayList<>();
	}

	public List<SwitchCaseLabel> getLabels() {
		return labels;
	}

	public void addLabel(SwitchCaseLabel label) {

		Objects.requireNonNull(label);
		
		labels.add(label);
	}
	
	public List<Statement> getStatements() {
		return statements;
	}

	@Override
	public void addStatement(Statement statement) {
		Objects.requireNonNull(statement);

		statements.add(statement);
	}
}
