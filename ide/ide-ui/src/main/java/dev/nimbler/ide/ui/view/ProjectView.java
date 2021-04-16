package dev.nimbler.ide.ui.view;

import dev.nimbler.build.types.resource.ResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.ui.view.KeyEventListener;
import dev.nimbler.ide.common.ui.view.View;

public interface ProjectView extends View {

	void refresh();
	
	void addListener(ProjectViewListener listener);

	void addKeyEventListener(KeyEventListener keyEventListener);

	void showSourceFile(SourceFileResourcePath sourceFile, boolean setFocus);

	ResourcePath getSelected();
}
