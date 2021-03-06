package dev.nimbler.exe.vm.bytecode.loader;

import java.io.IOException;

import dev.nimbler.language.bytecode.common.BytecodeFormat;
import dev.nimbler.language.bytecode.common.ClassByteCodeWithTypeSource;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.bytecode.common.ClassFileException;
import dev.nimbler.language.codemap.CodeMap.TypeResult;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.libs.ClassLibs;

public class LoadClassHelper {

	static ClassBytecode loadClass(TypeName typeName, TypeResult typeResult,
			BytecodeFormat bytecodeFormat, ClassLibs classLibs, LoaderMaps loaderMaps) throws IOException, ClassFileException {
	
		final LoadClassParameters<ClassLibs, Integer, LoadedClassesCache> parameters
			=  new LoadClassParameters<ClassLibs, Integer, LoadedClassesCache>(
				loaderMaps.typeMap,
				loaderMaps.codeMap,
				(type, typeSource, typeNo, classByteCode) -> typeNo,

				// Not entirely threadsafe since some other thread might be loading same class
				// However not important whether loading same class multiple times
				(typeNo, addedByteCode) -> loaderMaps.loadedClasses.addClass(typeNo, addedByteCode),
				
				type -> bytecodeFormat.loadClassBytecode(classLibs, type));
		
		return loadClassAndBaseTypesAndAddToCodeMap(typeName, typeResult, parameters);
	}
	

	public static <CLASSLIBS, TYPE, CACHE> ClassBytecode loadClassAndBaseTypesAndAddToCodeMap(
			TypeName typeName,
			TypeResult typeResult,
			LoadClassParameters<CLASSLIBS, TYPE, CACHE> parameters) throws IOException, ClassFileException {
		
		final ClassByteCodeWithTypeSource addedBytecode = parameters.typeMap.addOrGetType(
				typeName,
				parameters.codeMap,
				true,
				typeResult,
				parameters.createType, // (typeNo, classByteCode) -> typeNo,
				
				type -> {
		
			final ClassByteCodeWithTypeSource classBytecode = parameters.loadType.load(type);
			
			if (classBytecode != null) {
				loadBaseTypes(typeName, classBytecode, parameters);
			}
		
			return classBytecode;
		});
			
		
		if (addedBytecode != null) {
			final int methodCount = addedBytecode.getMethodCount();
			
			parameters.codeMap.setMethodCount(typeResult.type, methodCount);
			
			if (parameters.onAddedType != null) {
				parameters.onAddedType.accept(typeResult.type, addedBytecode);
			}
		}
	
		return addedBytecode;
	}
	
	private static <CLASSLIBS, TYPE, CACHE> void loadBaseTypes(
			TypeName typeName,
			ClassBytecode classBytecode,
			LoadClassParameters<CLASSLIBS, TYPE, CACHE> parameters) throws IOException, ClassFileException {
		
		ClassBytecode currentClass = classBytecode;
		
		final TypeResult typeResult = new TypeResult();
		
		for (;;) {

			for (int i = 0; i < currentClass.getImplementedInterfacesCount(); ++ i) {
				final TypeName interfaceTypeName = currentClass.getImplementedInterface(i);
				
				loadClassAndBaseTypesAndAddToCodeMap(interfaceTypeName, typeResult, parameters);
			}
			
			final TypeName superClassName = currentClass.getSuperClass();

			if (superClassName == null) {
				// root class
				break;
			}
			
			// System.out.println("## load superclass name " + superClassName);
			
			if (superClassName.equals(typeName)) {
				throw new IllegalStateException();
			}
			
			final ClassBytecode addedClass = loadClassAndBaseTypesAndAddToCodeMap(superClassName, typeResult, parameters);

			// System.out.println("## loaded superclass name " + superClassName);

			if (addedClass == null) {
				break;
			}
		}
	}
}
