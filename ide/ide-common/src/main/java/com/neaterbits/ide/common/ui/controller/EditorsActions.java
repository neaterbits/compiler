package com.neaterbits.ide.common.ui.controller;

import com.neaterbits.build.types.resource.SourceFileResourcePath;

public interface EditorsActions {
	
	void openSourceFileForEditing(SourceFileResourcePath sourceFile);
	
	void showCurrentEditedInProjectView();

	void closeCurrentEditedFile();

	void minMaxEditors();
}
