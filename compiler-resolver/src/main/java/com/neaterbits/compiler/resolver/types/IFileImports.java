package com.neaterbits.compiler.resolver.types;

import java.util.List;

import com.neaterbits.compiler.util.ScopedName;

public interface IFileImports {

	List<ScopedName> getAllNameCombinations(ScopedName scopedName);
	
}
