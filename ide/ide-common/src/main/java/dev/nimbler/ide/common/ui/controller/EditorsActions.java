package dev.nimbler.ide.common.ui.controller;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public interface EditorsActions {
	
	void openSourceFileForEditing(SourceFileResourcePath sourceFile);
	
	void showCurrentEditedInProjectView();

	void closeCurrentEditedFile();

	void minMaxEditors();
}
