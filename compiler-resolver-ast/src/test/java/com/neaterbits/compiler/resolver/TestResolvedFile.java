package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;

public class TestResolvedFile extends BaseTestFile<ResolvedType>
		implements ResolvedFile {

	@SafeVarargs
	public TestResolvedFile(String name, ResolvedType ... resolvedTypes) {
		super(name, resolvedTypes);
	}
}
