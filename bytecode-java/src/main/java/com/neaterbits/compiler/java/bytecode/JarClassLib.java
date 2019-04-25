package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.TypeSource;

final class JarClassLib extends JavaClassLib {

	private final File path;
	private final JarFile jarFile;
	
	JarClassLib(File path) throws IOException {
		this.path = path;
		this.jarFile = new JarFile(path);
	}

	@Override
	boolean hasClassName(TypeName className) {
		return jarFile.getJarEntry(toPath(className)) != null;
	}

	@Override
	InputStream openClassFile(TypeName className) throws IOException {
		
		Objects.requireNonNull(className);
		
		final String path = toPath(className);
		
		final JarEntry jarEntry = jarFile.getJarEntry(path);
		
		if (jarEntry == null) {
			throw new IllegalStateException("No jar entry for " + path + "/" + className.toDebugString());
		}
		
		return jarFile.getInputStream(jarEntry);
	}

	@Override
	DependencyFile getDependencyFile(TypeName className) {
		return new DependencyFile(path, TypeSource.LIBRARY);
	}

	@Override
	List<DependencyFile> getFiles() {
		return Arrays.asList(new DependencyFile(path, TypeSource.LIBRARY));
	}
}
