package dev.nimbler.compiler.util;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class FileSystemFileSpec implements FileSpec {

	private final File file;

	public FileSystemFileSpec(File file) {

		Objects.requireNonNull(file);
		
		this.file = file;
	}
	
	@Override
	public String getDistinctName() {
		try {
			return file.getCanonicalPath();
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public String getParseContextName() {
		return file.getName();
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
		FileSystemFileSpec other = (FileSystemFileSpec) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return file.getName();
	}
}
