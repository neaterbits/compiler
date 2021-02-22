package com.neaterbits.compiler.common.resolver;

import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;

public class TestResolvedFile extends BaseTestFile<ResolvedType> implements ResolvedFile {

	public TestResolvedFile(String name, ResolvedType ... resolvedTypes) {
		super(name, resolvedTypes);
	}
}
