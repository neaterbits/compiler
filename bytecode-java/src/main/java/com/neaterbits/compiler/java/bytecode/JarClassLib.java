package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.common.TypeName;

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
		return jarFile.getInputStream(jarFile.getJarEntry(toPath(className)));
	}

	@Override
	DependencyFile getDependencyFile(TypeName className) {
		return new DependencyFile(path, true);
	}
}
