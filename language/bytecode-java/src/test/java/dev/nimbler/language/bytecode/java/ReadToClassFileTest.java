package dev.nimbler.language.bytecode.java;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import dev.nimbler.language.bytecode.common.ClassFileException;
import dev.nimbler.language.bytecode.java.reader.BaseClassFileReaderTest;
import dev.nimbler.language.common.types.TypeVariant;

public class ReadToClassFileTest extends BaseClassFileReaderTest {

	/*
	@Test
	public void testReadClassFile() throws IOException, ClassFileException {
	
		readClass(new DebugReaderListener(new ClassFile()));
	}
	*/

	private ClassFile loadClassFileFromRTJar(String className) throws IOException, ClassFileException {

		final JavaBytecodeFormat javaByteCodeFormat = new JavaBytecodeFormat();
		
		final ClassFile classFile = new ClassFile();
		
		javaByteCodeFormat.loadClassBytecode(
		        JDKLibs.getJavaBaseLibraryPath(),
		        JDKLibs.parseClassName(className),
				// new DebugReaderListener(
						classFile
				//		)
				);
		
		return classFile;
	}
	
	@Test
	public void testLoadClassFromLibrary() throws IOException, ClassFileException {
		
		final ClassFile classFile = loadClassFileFromRTJar("java.util.HashMap");
		
		assertThat(classFile.getTypeVariant()).isEqualTo(TypeVariant.CLASS);
		
		assertThat(classFile.getSuperClass().getNamespace()).isEqualTo(new String [] { "java", "util" });
		assertThat(classFile.getSuperClass().getOuterTypes()).isNull();;
		assertThat(classFile.getSuperClass().getName()).isEqualTo("AbstractMap");
	}

	@Test
	public void testLoadClassInheritingObjectFromLibrary() throws IOException, ClassFileException {
		
		final ClassFile classFile = loadClassFileFromRTJar("java.util.AbstractMap");
		
		assertThat(classFile.getTypeVariant()).isEqualTo(TypeVariant.CLASS);

		assertThat(classFile.getSuperClass().getNamespace()).isEqualTo(new String [] { "java", "lang" });
		assertThat(classFile.getSuperClass().getOuterTypes()).isNull();;
		assertThat(classFile.getSuperClass().getName()).isEqualTo("Object");
	}

	@Test
	public void testLoadJavaLangObjectFromLibrary() throws IOException, ClassFileException {
		
		final ClassFile classFile = loadClassFileFromRTJar("java.lang.Object");
		
		assertThat(classFile.getTypeVariant()).isEqualTo(TypeVariant.CLASS);

		assertThat(classFile.getSuperClass()).isNull();
	}
}
