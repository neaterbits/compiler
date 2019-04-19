package com.neaterbits.compiler.codemap.compiler;

import com.neaterbits.compiler.codemap.CodeMap;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.util.TypeName;

public interface CompilerCodeMap extends CodeMap {

	int addFile(String file, int [] types);

	void addMapping(TypeName name, int typeNo);
	
	Integer getTypeNoByTypeName(TypeName typeName);
	
	void removeFile(String file);

	int addToken(int sourceFile, int tokenOffset, int tokenLength);

	default int addType(TypeVariant typeVariant, int numMethods, int [] thisExtendsFromClasses, int [] thisExtendsFromInterfaces) {
		
		final int typeNo = addType(typeVariant, thisExtendsFromClasses, thisExtendsFromInterfaces);
		
		setMethodCount(typeNo, numMethods);
		
		return typeNo;
	}

	default int getClassThisExtendsFrom(int typeNo) {
		return getExtendsFromSingleSuperClass(typeNo);
	}
}
