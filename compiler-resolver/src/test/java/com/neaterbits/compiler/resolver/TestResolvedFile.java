package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.resolver.loader.ResolvedFile;
import com.neaterbits.compiler.resolver.loader.ResolvedType;

public class TestResolvedFile extends BaseTestFile<ResolvedType> implements ResolvedFile {

	public TestResolvedFile(String name, ResolvedType ... resolvedTypes) {
		super(name, resolvedTypes);
	}
}
