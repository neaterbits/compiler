package dev.nimbler.ide.component.build.ui;

import java.util.List;

import dev.nimbler.build.types.compile.BuildIssue;
import dev.nimbler.ide.common.ui.view.View;

public interface BuildIssuesView extends View {

	void update(List<BuildIssue> buildIssues);
	
}
