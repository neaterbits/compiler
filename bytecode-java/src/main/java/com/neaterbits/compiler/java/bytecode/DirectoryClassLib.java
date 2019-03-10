package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.util.TypeName;

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

	private File getClassFile(TypeName className) {
		
		final File file = new File(directory, toPath(className));

		return file;
	}
	
	@Override
	InputStream openClassFile(TypeName className) throws IOException {
		
		return new FileInputStream(getClassFile(className));
	}

	@Override
	DependencyFile getDependencyFile(TypeName className) {
		return new DependencyFile(getClassFile(className), false);
	}

	@Override
	List<DependencyFile> getFiles() {
		
		final File [] classFiles = directory.listFiles(file -> file.getName().endsWith(".class"));
		
		return Arrays.stream(classFiles)
				.map(file -> new DependencyFile(file, false))
				.collect(Collectors.toList());
	}
}
