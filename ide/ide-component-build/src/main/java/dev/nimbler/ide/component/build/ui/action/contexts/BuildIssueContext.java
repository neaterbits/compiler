package dev.nimbler.ide.component.build.ui.action.contexts;

import java.util.Objects;

import dev.nimbler.build.types.compile.BuildIssue;
import dev.nimbler.ide.common.ui.actions.contexts.ActionContext;

public final class BuildIssueContext extends ActionContext {

	private final BuildIssue buildIssue;

	public BuildIssueContext(BuildIssue buildIssue) {

		Objects.requireNonNull(buildIssue);
		
		this.buildIssue = buildIssue;
	}

	public BuildIssue getBuildIssue() {
		return buildIssue;
	}
}
