package dev.nimbler.ide.component.search.ui;

import java.util.List;

import dev.nimbler.build.types.resource.SourceLineResourcePath;
import dev.nimbler.ide.common.ui.view.View;

public interface SearchResultsView extends View {

	void update(List<SourceLineResourcePath> sourceLines, SearchViewListener searchViewListener);
	
}
