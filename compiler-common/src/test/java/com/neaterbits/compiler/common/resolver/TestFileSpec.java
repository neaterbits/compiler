package com.neaterbits.compiler.common.resolver;

import com.neaterbits.compiler.common.loader.FileSpec;

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
