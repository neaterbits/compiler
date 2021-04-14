package dev.nimbler.ide.common.ui.model;

import java.util.LinkedHashMap;
import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public class EditedFilesModel {

	private final LinkedHashMap<SourceFileResourcePath, EditedFileModel> files;
	
	public EditedFilesModel() {
		this.files = new LinkedHashMap<>();
	}
	
	public void addFile(EditedFileModel fileModel) {
		
		Objects.requireNonNull(fileModel);
		
		files.put(fileModel.getSourceFile(), fileModel);
	}
}
