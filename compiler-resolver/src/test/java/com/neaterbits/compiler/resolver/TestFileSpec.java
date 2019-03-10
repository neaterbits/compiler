package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.resolver.types.FileSpec;

public class TestFileSpec implements FileSpec {
	private final String name;
	
	public TestFileSpec(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
