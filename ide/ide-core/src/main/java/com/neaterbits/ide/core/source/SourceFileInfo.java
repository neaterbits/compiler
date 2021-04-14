package com.neaterbits.ide.core.source;

import java.io.File;
import java.util.Objects;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.ide.component.common.language.LanguageComponent;

public class SourceFileInfo {

	private final SourceFileResourcePath path;
	private final LanguageComponent language;
	private final ResolvedTypes resolvedTypes; // for parser resolving of referenced types
	
	public SourceFileInfo(SourceFileResourcePath path, LanguageComponent language, ResolvedTypes resolvedTypes) {

		Objects.requireNonNull(path);
		Objects.requireNonNull(language);
		Objects.requireNonNull(resolvedTypes);
		
		this.path = path;
		this.language = language;
		this.resolvedTypes = resolvedTypes;
	}

	public SourceFileResourcePath getPath() {
		return path;
	}

	public LanguageComponent getLanguage() {
		return language;
	}

	public ResolvedTypes getResolvedTypes() {
		return resolvedTypes;
	}

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
