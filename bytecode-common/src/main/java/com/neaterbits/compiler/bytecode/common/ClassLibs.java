package com.neaterbits.compiler.bytecode.common;

import com.neaterbits.compiler.common.TypeName;

public interface ClassLibs {

	DependencyFile getDependencyFileFor(TypeName typeName);
	
	
}
