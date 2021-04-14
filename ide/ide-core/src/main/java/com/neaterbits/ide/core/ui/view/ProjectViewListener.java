package com.neaterbits.ide.core.ui.view;

import com.neaterbits.build.types.resource.SourceFileResourcePath;

public interface ProjectViewListener {

	void onSourceFileSelected(SourceFileResourcePath sourceFile);
	
}
