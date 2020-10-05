package com.neaterbits.compiler.bytecode.common;

import java.util.List;

import com.neaterbits.build.types.TypeName;

public interface ClassLibs {

	DependencyFile getDependencyFileFor(TypeName typeName);
	
	List<DependencyFile> getFiles();
	
}
