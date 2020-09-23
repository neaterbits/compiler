package com.neaterbits.compiler.codemap.compiler;

import com.neaterbits.compiler.codemap.CodeMap;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.CrossReferenceGetters;

public interface CompilerCodeMap extends CodeMap, CrossReferenceUpdater, CrossReferenceGetters, CompilerCodeMapGetters {

	int addFile(String file, int [] types);

	void addTypeMapping(TypeName name, int typeNo);
	
	Integer getTypeNoByTypeName(TypeName typeName);
	
	void removeFile(String file);

	default int addType(TypeVariant typeVariant, int numMethods, int [] thisExtendsFromClasses, int [] thisExtendsFromInterfaces) {
		
		final int typeNo = addType(typeVariant, thisExtendsFromClasses, thisExtendsFromInterfaces);
		
		setMethodCount(typeNo, numMethods);
		
		return typeNo;
	}

	default int getClassThisExtendsFrom(int typeNo) {
		return getExtendsFromSingleSuperClass(typeNo);
	}
}
