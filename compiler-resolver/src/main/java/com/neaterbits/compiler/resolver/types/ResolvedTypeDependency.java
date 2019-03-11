package com.neaterbits.compiler.resolver.types;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

public interface ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	TypeName getCompleteName();
	
	default ScopedName getScopedName() {
		return getCompleteName().toScopedName();
	}
	
	ReferenceType getReferenceType();
	
	TypeVariant getTypeVariant();
	
	boolean shouldUpdateOnResolve();
	
	void updateOnResolve(COMPLEXTYPE type);

	void replaceWithComplexType(COMPLEXTYPE complexType);
	
	void replaceWithBuiltinType(BUILTINTYPE builtinType);
}
