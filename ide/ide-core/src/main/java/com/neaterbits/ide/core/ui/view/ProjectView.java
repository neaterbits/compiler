package com.neaterbits.ide.core.ui.view;

import com.neaterbits.build.types.resource.ResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.view.View;

public interface ProjectView extends View {

	void refresh();
	
	void addListener(ProjectViewListener listener);

	void addKeyEventListener(KeyEventListener keyEventListener);

	void showSourceFile(SourceFileResourcePath sourceFile, boolean setFocus);

	ResourcePath getSelected();
}
