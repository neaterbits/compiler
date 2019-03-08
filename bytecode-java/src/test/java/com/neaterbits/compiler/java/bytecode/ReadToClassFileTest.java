package com.neaterbits.compiler.java.bytecode;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.bytecode.common.loader.HashTypeMap;
import com.neaterbits.compiler.bytecode.common.loader.LoadClassHelper;
import com.neaterbits.compiler.bytecode.common.loader.LoadClassParameters;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap;
import com.neaterbits.compiler.common.resolver.codemap.IntCodeMap;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.compiler.java.bytecode.reader.BaseClassFileReaderTest;

public class ReadToClassFileTest extends BaseClassFileReaderTest {

	/*
	@Test
	public void testReadClassFile() throws IOException, ClassFileException {
	
		readClass(new DebugReaderListener(new ClassFile()));
	}
	*/
	
	private static File getRTJarPath() {
		
		final String jreDir = System.getProperty("java.home");
		
		final File file = new File(jreDir + "/lib/rt.jar");

		return file;
	}
	
	private static TypeName parseClassName(String className) {

		final String [] parts = Strings.split(className, '.');
		
		return new TypeName(
				Arrays.copyOf(parts, parts.length - 1),
				null,
				parts[parts.length - 1]);
	}
	
	private ClassFile loadClassFileFromRTJar(String className) throws IOException, ClassFileException {

		final File file = getRTJarPath();
		final JavaBytecodeFormat javaByteCodeFormat = new JavaBytecodeFormat();
		
		final ClassFile classFile = new ClassFile();
		
		javaByteCodeFormat.loadClassBytecode(file, parseClassName(className),
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

	private static class TypeInfo {
		private final int typeNo;
		private final TypeName typeName;

		public TypeInfo(int typeNo, TypeName typeName) {
			this.typeNo = typeNo;
			this.typeName = typeName;
		}
	}
	
	@Test
	public void testLoadClassHelper() throws IOException, ClassFileException {
		
		final HashTypeMap<TypeInfo> typeMap = new HashTypeMap<>(type -> type.typeNo);
		final CodeMap codeMap = new IntCodeMap();

		final File file = getRTJarPath();
		final JavaBytecodeFormat javaBytecodeFormat = new JavaBytecodeFormat();

		final LoadClassParameters<File, TypeInfo, Void> parameters = new LoadClassParameters<>(
				typeMap,
				codeMap,
				(typeName, typeNo, classByteCode) -> {

					System.out.println("## create type for " + typeName.toDebugString());
					
					return new TypeInfo(typeNo, typeName);
				},
				null,
				(typeName) -> javaBytecodeFormat.loadClassBytecode(file, typeName));
		
		LoadClassHelper.loadClassAndBaseClassesAndAddToCodeMap(
				parseClassName("java.util.HashMap"),
				new CodeMap.TypeResult(),
				parameters);
	}
}
