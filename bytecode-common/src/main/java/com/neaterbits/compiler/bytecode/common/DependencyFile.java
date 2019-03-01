package com.neaterbits.compiler.bytecode.common;

import java.io.File;
import java.util.Objects;

public final class DependencyFile {

	private final File file;
	private final boolean isLibraryFile;

	public DependencyFile(File file, boolean isLibraryFile) {
		
		Objects.requireNonNull(file);
		
		this.file = file;
		this.isLibraryFile = isLibraryFile;
	}
	
	public File getFile() {
		return file;
	}

	public boolean isLibraryFile() {
		return isLibraryFile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
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
		DependencyFile other = (DependencyFile) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}
}
