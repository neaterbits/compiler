package com.neaterbits.ide.swt;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.ui.config.TextEditorConfig;
import com.neaterbits.ide.core.ui.view.EditorView;
import com.neaterbits.ide.ui.swt.SWTView;

abstract class SWTEditorView extends SWTView implements EditorView {

	SWTEditorView() {
		
	}

	abstract void setSelectedAndFocused();

	abstract void close();
	
	abstract void configure(TextEditorConfig config);

	abstract SourceFileResourcePath getSourceFile();
	
}
