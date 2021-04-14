package com.neaterbits.exe.vm.bytecode.loader;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.neaterbits.exe.vm.bytecode.loader.HashTypeMap;
import com.neaterbits.exe.vm.bytecode.loader.LoadClassHelper;
import com.neaterbits.exe.vm.bytecode.loader.LoadClassParameters;
import com.neaterbits.language.bytecode.common.ClassBytecode;
import com.neaterbits.language.bytecode.common.ClassFileException;
import com.neaterbits.language.bytecode.java.JDKLibs;
import com.neaterbits.language.bytecode.java.JavaBytecodeFormat;
import com.neaterbits.language.codemap.CodeMap;
import com.neaterbits.language.codemap.DynamicMethodOverrideMap;
import com.neaterbits.language.codemap.IntCodeMap;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.types.TypeVariant;
import com.neaterbits.language.common.typesources.TypeSource;

public class LoadClassHelperTest {

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
				JDKLibs.parseClassName(className),
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

		final TypeName javaUtilHashMap = JDKLibs.parseClassName("java.util.HashMap");
		final TypeName javaUtilAbstractMap = JDKLibs.parseClassName("java.util.AbstractMap");
		final TypeName javaLangObject = JDKLibs.parseClassName("java.lang.Object");
		
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
}
