package dev.nimbler.ide.component.search.ui;

import dev.nimbler.build.types.resource.SourceLineResourcePath;

public interface SearchViewListener {

	void onSearchResultSelected(SourceLineResourcePath sourceLinePath);
	
}
