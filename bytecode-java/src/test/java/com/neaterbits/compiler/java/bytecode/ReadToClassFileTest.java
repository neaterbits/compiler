package com.neaterbits.compiler.java.bytecode;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.bytecode.common.loader.HashTypeMap;
import com.neaterbits.compiler.bytecode.common.loader.LoadClassHelper;
import com.neaterbits.compiler.bytecode.common.loader.LoadClassParameters;
import com.neaterbits.compiler.codemap.CodeMap;
import com.neaterbits.compiler.codemap.DynamicMethodOverrideMap;
import com.neaterbits.compiler.codemap.IntCodeMap;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.java.bytecode.reader.BaseClassFileReaderTest;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.TypeSource;

public class ReadToClassFileTest extends BaseClassFileReaderTest {

	/*
	@Test
	public void testReadClassFile() throws IOException, ClassFileException {
	
		readClass(new DebugReaderListener(new ClassFile()));
	}
	*/
	
	
	private static TypeName parseClassName(String className) {

		final String [] parts = Strings.split(className, '.');
		
		return new TypeName(
				Arrays.copyOf(parts, parts.length - 1),
				null,
				parts[parts.length - 1]);
	}
	
	private ClassFile loadClassFileFromRTJar(String className) throws IOException, ClassFileException {

		final JavaBytecodeFormat javaByteCodeFormat = new JavaBytecodeFormat();
		
		final ClassFile classFile = new ClassFile();
		
		javaByteCodeFormat.loadClassBytecode(
		        JDKLibs.getJavaBaseLibraryPath(),
		        parseClassName(className),
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
		private final TypeSource typeSource;

		public TypeInfo(int typeNo, TypeName typeName, TypeSource typeSource) {
			this.typeNo = typeNo;
			this.typeName = typeName;
			this.typeSource = typeSource;
		}
	}
	
	private ClassBytecode loadClassAndBaseTypes(String className, HashTypeMap<TypeInfo> typeMap, CodeMap codeMap) throws IOException, ClassFileException {

		final JavaBytecodeFormat javaBytecodeFormat = new JavaBytecodeFormat();

		final File library = JDKLibs.getJavaBaseLibraryPath();
		
		final LoadClassParameters<File, TypeInfo, Void> parameters = new LoadClassParameters<>(
				typeMap,
				codeMap,
				(typeName, typeSource, typeNo, classByteCode) -> {
					
					return new TypeInfo(typeNo, typeName, typeSource);
				},
				null,
				(typeName) -> javaBytecodeFormat.loadClassBytecode(library, typeName, TypeSource.LIBRARY));
		
		return LoadClassHelper.loadClassAndBaseTypesAndAddToCodeMap(
				parseClassName(className),
				new CodeMap.TypeResult(),
				parameters);
	}
	
	@Test
	public void testLoadClassHelper() throws IOException, ClassFileException {
		
		final HashTypeMap<TypeInfo> typeMap = new HashTypeMap<>(type -> type.typeNo, type -> type.typeSource);
		final CodeMap codeMap = new IntCodeMap(new DynamicMethodOverrideMap());

		final ClassBytecode classBytecode = loadClassAndBaseTypes("java.util.HashMap", typeMap, codeMap);
		
		assertThat(classBytecode).isNotNull();
		
		assertThat(classBytecode.getTypeVariant()).isEqualTo(TypeVariant.CLASS);

		final TypeName javaUtilHashMap = parseClassName("java.util.HashMap");
		final TypeName javaUtilAbstractMap = parseClassName("java.util.AbstractMap");
		final TypeName javaLangObject = parseClassName("java.lang.Object");
		
		final int hashMapTypeNo = typeMap.getTypeNo(javaUtilHashMap);
		final TypeInfo hashMapTypeInfo = typeMap.getType(javaUtilHashMap);

		assertThat(hashMapTypeInfo).isNotNull();
		assertThat(hashMapTypeInfo.typeNo).isEqualTo(hashMapTypeNo);
		assertThat(hashMapTypeInfo.typeName).isEqualTo(javaUtilHashMap);

		final TypeInfo abstractMapTypeInfo = typeMap.getType(javaUtilAbstractMap);
		assertThat(abstractMapTypeInfo).isNotNull();

		final TypeInfo objectTypeInfo = typeMap.getType(javaLangObject);
		assertThat(objectTypeInfo).isNotNull();

		assertThat(hashMapTypeInfo.typeNo).isGreaterThan(abstractMapTypeInfo.typeNo);
		assertThat(hashMapTypeInfo.typeNo).isGreaterThan(objectTypeInfo.typeNo);
		assertThat(abstractMapTypeInfo.typeNo).isGreaterThan(objectTypeInfo.typeNo);
		
		assertThat(codeMap.getExtendsFromSingleSuperClass(hashMapTypeNo)).isEqualTo(abstractMapTypeInfo.typeNo);
		assertThat(codeMap.getExtendsFromSingleSuperClass(abstractMapTypeInfo.typeNo)).isEqualTo(objectTypeInfo.typeNo);

		final int [] typesObjectExtendsFrom = codeMap.getTypesThisDirectlyExtends(objectTypeInfo.typeNo);
		assertThat(typesObjectExtendsFrom.length).isEqualTo(0);
		
		final int [] typesExtendingObject = codeMap.getTypesDirectlyExtendingThis(objectTypeInfo.typeNo);
		assertThat(typesExtendingObject).isNotNull();
		assertThat(typesExtendingObject.length).isEqualTo(1);

		assertThat(typesExtendingObject[0]).isEqualTo(abstractMapTypeInfo.typeNo);
		
		final int [] typesExtendingAbstractMap = codeMap.getTypesDirectlyExtendingThis(abstractMapTypeInfo.typeNo);
		assertThat(typesExtendingAbstractMap.length).isEqualTo(1);
		assertThat(typesExtendingAbstractMap[0]).isEqualTo(hashMapTypeInfo.typeNo);
	}
	
	/*
	private static final boolean contains(int [] array, int value) {
		
		for (int i = 0; i < array.length; ++ i) {
			if (array[i] == value) {
				return true;
			}
		}
		
		return false;
	}
	*/
}
