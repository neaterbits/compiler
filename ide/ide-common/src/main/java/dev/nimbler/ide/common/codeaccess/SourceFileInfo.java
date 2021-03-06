package dev.nimbler.ide.common.codeaccess;

import java.io.File;
import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.codeaccess.types.LanguageName;

public class SourceFileInfo {

	private final SourceFileResourcePath path;
	private final LanguageName language;
	//private final ResolvedTypes resolvedTypes; // for parser resolving of referenced types
	
	public SourceFileInfo(SourceFileResourcePath path, LanguageName language /* , ResolvedTypes resolvedTypes */) {

		Objects.requireNonNull(path);
		Objects.requireNonNull(language);
		// Objects.requireNonNull(resolvedTypes);
		
		this.path = path;
		this.language = language;
		// this.resolvedTypes = resolvedTypes;
	}

	public SourceFileResourcePath getPath() {
		return path;
	}

	public LanguageName getLanguage() {
		return language;
	}

	/*
	public ResolvedTypes getResolvedTypes() {
		return resolvedTypes;
	}
*/
	public File getFile() {
		return path.getFile();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SourceFileInfo other = (SourceFileInfo) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}
