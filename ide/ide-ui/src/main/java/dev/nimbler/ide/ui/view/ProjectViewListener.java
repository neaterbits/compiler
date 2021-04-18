package dev.nimbler.ide.ui.view;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public interface ProjectViewListener {

	void onSourceFileSelected(SourceFileResourcePath sourceFile);
	
}
