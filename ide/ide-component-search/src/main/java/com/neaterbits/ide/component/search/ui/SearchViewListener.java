package com.neaterbits.ide.component.search.ui;

import com.neaterbits.build.types.resource.SourceLineResourcePath;

public interface SearchViewListener {

	void onSearchResultSelected(SourceLineResourcePath sourceLinePath);
	
}
