package dev.nimbler.ide.common.ui.model;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public final class EditedFileModel {

	private final SourceFileResourcePath sourceFile;
	
	private String text;
	
	public EditedFileModel(SourceFileResourcePath sourceFile) {

		Objects.requireNonNull(sourceFile);

		this.sourceFile = sourceFile;
	}
	
	public SourceFileResourcePath getSourceFile() {
		return sourceFile;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
