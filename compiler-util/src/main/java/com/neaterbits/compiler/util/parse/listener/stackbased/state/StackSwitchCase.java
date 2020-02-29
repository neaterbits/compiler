package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackSwitchCase<EXPRESSION, PRIMARY extends EXPRESSION, VARIABLE_REFERENCE extends PRIMARY, SWITCH_CASE_GROUP>
	extends StackExpressionList<EXPRESSION, PRIMARY, VARIABLE_REFERENCE> {

	private final List<SWITCH_CASE_GROUP> groups;
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

	public void addGroup(SWITCH_CASE_GROUP group) {

		Objects.requireNonNull(group);
		
		groups.add(group);
	}
	
	public List<SWITCH_CASE_GROUP> getGroups() {
		return groups;
	}
}
