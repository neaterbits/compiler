package com.neaterbits.compiler.bytecode.common.loader;

import java.io.IOException;

import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.bytecode.common.ClassLibs;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap.TypeResult;

class LoadClassHelper {

	static ClassBytecode loadClass(TypeName typeName, TypeResult typeResult,
			BytecodeFormat bytecodeFormat, ClassLibs classLibs, LoaderMaps loaderMaps) {
		
		final ClassBytecode addedBytecode = loaderMaps.typeMap.addOrGetType(
				typeName,
				loaderMaps.codeMap,
				typeResult,
				(typeNo, classByteCode) -> typeNo,
				type -> {
		
			// Not entirely threadsafe since some other thread might be loading same class
			// However not important whether loading same class multiple times
			ClassBytecode classBytecode = null;
			
			try {
				classBytecode = bytecodeFormat.loadClassBytecode(classLibs, type);
			} catch (IOException | ClassFileException ex) {
				ex.printStackTrace();
			}
			
			if (classBytecode != null) {
				loadBaseClasses(classBytecode, bytecodeFormat, classLibs, loaderMaps);
			}
		
			return classBytecode;
		});
			
		
		if (addedBytecode != null) {
			final int methodCount = addedBytecode.getMethodCount();
			
			loaderMaps.codeMap.setMethodCount(typeResult.type, methodCount);
			loaderMaps.loadedClasses.addClass(typeResult.type, addedBytecode);
		}
	
		return addedBytecode;
	}
	
	static void loadBaseClasses(ClassBytecode classBytecode, BytecodeFormat bytecodeFormat, ClassLibs classLibs, LoaderMaps loaderMaps) {
		
		ClassBytecode currentClass = classBytecode;
		
		final TypeResult typeResult = new TypeResult();
		
		for (;;) {
			final TypeName superClassName = currentClass.getSuperClass();
			
			final ClassBytecode addedClass = loadClass(superClassName, typeResult, bytecodeFormat, classLibs, loaderMaps);

			if (addedClass == null) {
				break;
			}
		}
	}

}
