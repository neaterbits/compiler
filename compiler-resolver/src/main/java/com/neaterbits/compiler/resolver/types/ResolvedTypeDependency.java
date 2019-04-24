package com.neaterbits.compiler.resolver.types;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.UpdateOnResolve;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

public interface ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {

	TypeName getCompleteName();
	
	default ScopedName getScopedName() {
		return getCompleteName().toScopedName();
	}
	
	ReferenceType getReferenceType();
	
	TypeVariant getTypeVariant();
	
	int getTypeReferenceElement();
	
	TypeResolveMode getTypeResolveMode();
	
	boolean shouldUpdateOnResolve();
	
	UpdateOnResolve getUpdateOnResolve();
	
	Integer getUpdateOnResolveElementRef();
}
