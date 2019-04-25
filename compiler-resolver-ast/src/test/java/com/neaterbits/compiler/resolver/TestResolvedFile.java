package com.neaterbits.compiler.resolver;

import java.util.Arrays;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.NameFileSpec;

public class TestResolvedFile extends ResolvedFile {

	public TestResolvedFile(String name, ResolvedType ... resolvedTypes) {
		super(new NameFileSpec(name), Arrays.asList(resolvedTypes));
	}
}
