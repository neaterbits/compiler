package dev.nimbler.build.types.resource;

import java.io.File;

import dev.nimbler.build.types.language.Language;

public final class SourceFolderResource extends SourceFileHolderResource {

	private final Language language;

	public SourceFolderResource(File file, String name, Language language) {
		super(file, name);

		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}
}
