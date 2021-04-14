package dev.nimbler.ide.swt;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.ui.config.TextEditorConfig;
import dev.nimbler.ide.core.ui.view.EditorView;
import dev.nimbler.ide.ui.swt.SWTView;

abstract class SWTEditorView extends SWTView implements EditorView {

	SWTEditorView() {
		
	}

	abstract void setSelectedAndFocused();

	abstract void close();
	
	abstract void configure(TextEditorConfig config);

	abstract SourceFileResourcePath getSourceFile();
	
}
