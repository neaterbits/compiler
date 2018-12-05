package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.bytecode.common.ClassLibs;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.java.bytecode.reader.ClassFileReader;

public class JavaClassLibs implements ClassLibs {

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
	public ClassBytecode loadClassBytecode(TypeName className) throws IOException, ClassFileException {
		
		Objects.requireNonNull(className);
		
		final JavaClassLib classLib = classpath.stream()
				.filter(lib -> lib.hasClassName(className))
				.findFirst()
				.orElse(null);
		
		
		return classLib != null ? loadClassByteCode(className, classLib.openClassFile(className)) : null;
	}

	private static ClassBytecode loadClassByteCode(TypeName className, InputStream inputStream) throws IOException, ClassFileException {
		
		final ClassFile classFile = new ClassFile();
		
		try {
			ClassFileReader.readClassFile(inputStream, classFile);
		}
		finally {
			inputStream.close();
		}
		
		return classFile;
	}
}

