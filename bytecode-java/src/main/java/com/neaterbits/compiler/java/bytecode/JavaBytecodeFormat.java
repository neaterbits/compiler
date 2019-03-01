package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.bytecode.common.ClassLibs;
import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.compiler.java.bytecode.reader.ClassFileReader;

public class JavaBytecodeFormat implements BytecodeFormat {

	@Override
	public Set<TypeName> getTypesFromLibraryFile(File libraryPath) throws IOException {

		final Set<TypeName> files;
		
		try (JarFile jarFile = new JarFile(libraryPath)) {
		
			files = jarFile.stream()
				.map(entry -> entry.getName())
				.filter(name -> name.endsWith(".class"))
				.map(name -> Strings.split(name, '/'))
				.map(parts -> new TypeName(
						Arrays.copyOf(parts, parts.length - 1),
						null,
						parts[parts.length - 1]))
				.collect(Collectors.toSet());
		
		}
		
		return files;
	}
	
	@Override
	public ClassBytecode loadClassBytecode(ClassLibs classLibs, TypeName className) throws IOException, ClassFileException {
		
		Objects.requireNonNull(className);
		
		final DependencyFile file = classLibs.getDependencyFileFor(className);
		
		final ClassBytecode classBytecode;
		
		if (file == null) {
			classBytecode = null;
		}
		else {
			
			final JavaClassLib classLib = file.isLibraryFile()
					? new JarClassLib(file.getFile())
					: new DirectoryClassLib(file.getFile().getParentFile());
			
					
					
			classBytecode = loadClassByteCode(className, classLib.openClassFile(className));
		}

		return classBytecode;
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
