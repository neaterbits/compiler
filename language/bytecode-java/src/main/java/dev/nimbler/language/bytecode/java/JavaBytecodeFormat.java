package dev.nimbler.language.bytecode.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.jutils.Strings;

import dev.nimbler.language.bytecode.common.BytecodeFormat;
import dev.nimbler.language.bytecode.common.ClassByteCodeWithTypeSource;
import dev.nimbler.language.bytecode.common.ClassFileException;
import dev.nimbler.language.bytecode.java.reader.ClassFileReader;
import dev.nimbler.language.bytecode.java.reader.ClassFileReaderListener;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.DependencyFile;
import dev.nimbler.language.common.typesources.TypeSource;
import dev.nimbler.language.common.typesources.libs.ClassLibs;

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
						removeClassSuffix(parts[parts.length - 1])))
				.collect(Collectors.toSet());
		
		}
		
		return files;
	}
	
	private static String removeClassSuffix(String name) {
		return name.substring(0, name.length() - ".class".length());
	}
	
	@Override
	public ClassByteCodeWithTypeSource loadClassBytecode(ClassLibs classLibs, TypeName className) throws IOException, ClassFileException {
		
		Objects.requireNonNull(className);
		
		final DependencyFile file = classLibs.getDependencyFileFor(className);
		
		final ClassByteCodeWithTypeSource classBytecode;
		
		if (file == null) {
			classBytecode = null;
		}
		else {
			final JavaClassLib classLib = file.isLibraryFile()
					? new JarClassLib(file.getFile())
					: new DirectoryClassLib(file.getFile().getParentFile());
					

			final ClassFileWithTypeSource classFile = new ClassFileWithTypeSource(file.getTypeSource());
			
			loadClassByteCode(className, classLib.openClassFile(className), classFile);
			
			classBytecode = classFile;
		}

		return classBytecode;
	}

	
	@Override
	public ClassByteCodeWithTypeSource loadClassBytecode(InputStream inputStream, TypeSource typeSource) throws IOException, ClassFileException {

		final ClassFileWithTypeSource classFile = new ClassFileWithTypeSource(typeSource);
		
		JavaBytecodeFormat.loadClassByteCode(inputStream, classFile);
		
		return classFile;
	}
	
	private static BaseZipFileClassLib<?, ?> getClassLib(File library) throws IOException {
	    
	    return library.getName().endsWith(".jmod")
                ? new JModClassLib(library)
                : new JarClassLib(library);
	}
	
	@Override
	public ClassByteCodeWithTypeSource loadClassBytecode(File library, TypeName typeName, TypeSource typeSource) throws IOException, ClassFileException {

		Objects.requireNonNull(library);
		Objects.requireNonNull(typeName);

        final BaseZipFileClassLib<?, ?> libraryFile = getClassLib(library);
		
		final ClassFileWithTypeSource classFile = new ClassFileWithTypeSource(typeSource);
		
		loadClassByteCodeAndCloseStream(libraryFile.openClassFile(typeName), classFile);
		
		return classFile;
	}

	void loadClassBytecode(File library, TypeName typeName, ClassFileReaderListener readerListener) throws IOException, ClassFileException {

		final BaseZipFileClassLib<?, ?> libraryFile = getClassLib(library);

		loadClassByteCodeAndCloseStream(libraryFile.openClassFile(typeName), readerListener);
	}

	private static void loadClassByteCode(TypeName className, InputStream inputStream, ClassFileReaderListener readerListener) throws IOException, ClassFileException {
		loadClassByteCode(inputStream, readerListener);
	}

	private static void loadClassByteCode(InputStream inputStream, ClassFileReaderListener readerListener) throws IOException, ClassFileException {
		ClassFileReader.readClassFile(inputStream, readerListener);
	}
		
	private static void loadClassByteCodeAndCloseStream(InputStream inputStream, ClassFileReaderListener readerListener) throws IOException, ClassFileException {
		
		try {
			loadClassByteCode(inputStream, readerListener);
		}
		finally {
			inputStream.close();
		}
	}
}
