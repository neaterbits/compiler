package com.neaterbits.compiler.resolver;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;

public abstract class BaseTestFile<TYPE> {

	private final FileSpec spec;
	private final List<TYPE> types;

	protected BaseTestFile(FileSpec spec, List<TYPE> types) {
		this.spec = spec;
		this.types = types;
	}

	protected BaseTestFile(FileSpec spec, TYPE [] types) {
		this(spec, Arrays.asList(types));
	}

	protected BaseTestFile(String name, TYPE [] types) {
		this(new NameFileSpec(name), Arrays.asList(types));
	}

	public final FileSpec getSpec() {
		return spec;
	}

	public final List<TYPE> getTypes() {
		return types;
	}
}
