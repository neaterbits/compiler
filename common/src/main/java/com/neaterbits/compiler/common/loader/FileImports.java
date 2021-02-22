package com.neaterbits.compiler.common.loader;

import java.util.List;

import com.neaterbits.compiler.common.ast.ScopedName;

public interface FileImports {

	List<ScopedName> getAllNameCombinations(ScopedName scopedName);
	
}
