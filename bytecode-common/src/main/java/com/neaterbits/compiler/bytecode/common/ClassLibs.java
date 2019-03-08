package com.neaterbits.compiler.bytecode.common;

import java.util.List;

import com.neaterbits.compiler.common.TypeName;

public interface ClassLibs {

	DependencyFile getDependencyFileFor(TypeName typeName);
	
	List<DependencyFile> getFiles();
	
}
