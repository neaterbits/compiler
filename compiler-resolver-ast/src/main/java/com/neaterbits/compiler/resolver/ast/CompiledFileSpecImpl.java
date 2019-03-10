package com.neaterbits.compiler.resolver.ast;

import java.io.File;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.FileSpec;

final class CompiledFileSpecImpl implements FileSpec {

	private final File file;

	CompiledFileSpecImpl(File file) {

		Objects.requireNonNull(file);
		
		this.file = file;
	}
	
	@Override
	public String getName() {
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
		CompiledFileSpecImpl other = (CompiledFileSpecImpl) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return file.getName();
	}
}
