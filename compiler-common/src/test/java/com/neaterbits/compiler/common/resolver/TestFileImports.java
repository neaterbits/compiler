package com.neaterbits.compiler.common.resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.IFileImports;

public final class TestFileImports implements IFileImports {

	private final Map<ScopedName, List<ScopedName>> nameCombinations;
	
	public TestFileImports() {
		this.nameCombinations = new HashMap<>();
	}
	
	public void addMapping(ScopedName from, List<ScopedName> to) {
		Objects.requireNonNull(from);
		Objects.requireNonNull(to);
		
		nameCombinations.put(from, to);
	}
	
	@Override
	public List<ScopedName> getAllNameCombinations(ScopedName scopedName) {
		
		Objects.requireNonNull(scopedName);
		
		return nameCombinations.get(scopedName);
	}
}
