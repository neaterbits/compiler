package dev.nimbler.ide.core.ui.view;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.ui.view.View;
import dev.nimbler.ide.util.ui.text.styling.TextStylingModel;

public interface EditorsView extends View {

	EditorView displayFile(SourceFileResourcePath sourceFile, TextStylingModel textStylingModel, EditorSourceActionContextProvider editorSourceActionContextProvider);

	void closeFile(SourceFileResourcePath sourceFile);
	
	SourceFileResourcePath getCurrentEditedFile();
}
