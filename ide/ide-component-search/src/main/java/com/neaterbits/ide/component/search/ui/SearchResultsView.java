package com.neaterbits.ide.component.search.ui;

import java.util.List;

import com.neaterbits.build.types.resource.SourceLineResourcePath;
import com.neaterbits.ide.common.ui.view.View;

public interface SearchResultsView extends View {

	void update(List<SourceLineResourcePath> sourceLines, SearchViewListener searchViewListener);
	
}
