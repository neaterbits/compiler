package dev.nimbler.ide.core.ui.view;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public interface ProjectViewListener {

	void onSourceFileSelected(SourceFileResourcePath sourceFile);
	
}
