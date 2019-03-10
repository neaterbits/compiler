package com.neaterbits.compiler.resolver.loader;

import java.util.List;

import com.neaterbits.compiler.util.ScopedName;

public interface IFileImports {

	List<ScopedName> getAllNameCombinations(ScopedName scopedName);
	
}
