package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.neaterbits.compiler.common.TypeName;

final class DirectoryClassLib extends JavaClassLib {
	
	private final File directory;
	
	DirectoryClassLib(File directory) {
		
		Objects.requireNonNull(directory);
		
		this.directory = directory;
	}

	@Override
	boolean hasClassName(TypeName className) {
		
		final File file = new File(directory, toPath(className));
		
		return file.exists() && file.isFile() && file.canRead();
	}

	@Override
	InputStream openClassFile(TypeName className) throws IOException {
		
		final File file = new File(directory, toPath(className));

		return new FileInputStream(file);
	}
}
