package com.neaterbits.compiler.common.loader;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;

public final class LoadSpec {

	// Possible imports for this name where this file might be found
	// eg. search paths for file
	private final ScopedName name;
	private final List<ScopedName> imports;

	public LoadSpec(ScopedName name, List<ScopedName> imports) {
		
		Objects.requireNonNull(name);
		
		this.name = name;
		this.imports = imports;
	}

	public ScopedName getName() {
		return name;
	}

	public List<ScopedName> getImports() {
		return imports;
	}
}
