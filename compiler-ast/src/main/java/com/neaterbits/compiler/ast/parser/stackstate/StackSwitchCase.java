package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.statement.SwitchCaseGroup;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackSwitchCase extends StackExpressionList {

	private final List<SwitchCaseGroup> groups;
	
	public StackSwitchCase(ParseLogger parseLogger) {
		super(parseLogger);

		this.groups = new ArrayList<>();
	}

	public void addGroup(SwitchCaseGroup group) {

		Objects.requireNonNull(group);
		
		groups.add(group);
	}
	
	public List<SwitchCaseGroup> getGroups() {
		return groups;
	}
}
