package com.neaterbits.compiler.common.loader;

import com.neaterbits.compiler.common.ast.ScopedName;

public interface TypeInfo {
	
	ScopedName getScopedName();
	
	TypeVariant getTypeVariant();

	boolean isInterface();
	
	boolean isClass();
	
	boolean isEnum();
}
