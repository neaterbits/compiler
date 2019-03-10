package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.bytecode.common.ClassLibs;
import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.util.TypeName;

public final class JavaClassLibs implements ClassLibs {

	private final List<JavaClassLib> classpath;
	
	public JavaClassLibs(List<String> classpath) throws IOException {

		this.classpath = new ArrayList<>(classpath.size());
		
		for (String path : classpath) {
			final File file = new File(path);

			final JavaClassLib classLib;
			
			if (!file.exists()) {
				throw new IllegalArgumentException("No such file " + file);
			}
			else if (file.isDirectory()) {
				classLib = new DirectoryClassLib(file);
			}
			else if (file.isFile()) {
				classLib = new JarClassLib(file);
			}
			else {
				throw new IllegalArgumentException("Neither file nor directory " + file);
			}
			
			this.classpath.add(classLib);
		}
	}

	@Override
	public DependencyFile getDependencyFileFor(TypeName typeName) {
		
	   final JavaClassLib classLib = classpath.stream()
	                           .filter(lib -> lib.hasClassName(typeName))
	                           .findFirst()
	                           .orElse(null);
		
	   
	   return classLib.getDependencyFile(typeName);
	}

	@Override
	public List<DependencyFile> getFiles() {

		final List<DependencyFile> files = new ArrayList<>();
		
		for (JavaClassLib classLib : classpath) {
			files.addAll(classLib.getFiles());
		}
		
		return files;
	}
}

