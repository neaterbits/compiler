package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.statement.SwitchCaseGroup;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackSwitchCase extends StackExpressionList {

	private final List<SwitchCaseGroup> groups;
	private final String keyword;
	private final Context keywordContext;
	
	public StackSwitchCase(ParseLogger parseLogger, String keyword, Context keywordContext) {
		super(parseLogger);

		this.groups = new ArrayList<>();
		this.keyword = keyword;
		this.keywordContext = keywordContext;
	}

	public String getKeyword() {
		return keyword;
	}

	public Context getKeywordContext() {
		return keywordContext;
	}

	public void addGroup(SwitchCaseGroup group) {

		Objects.requireNonNull(group);
		
		groups.add(group);
	}
	
	public List<SwitchCaseGroup> getGroups() {
		return groups;
	}
}
