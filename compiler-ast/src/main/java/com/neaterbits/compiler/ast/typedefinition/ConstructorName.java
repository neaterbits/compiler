package com.neaterbits.compiler.ast.typedefinition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.ast.block.CallableName;

public final class ConstructorName extends CallableName {

	private final List<String> names;
	
	public ConstructorName(String name) {
		this(Arrays.asList(name));
	}
	
	public ConstructorName(ClassName className) {
		this(className.getName());
	}
	
	public ConstructorName(List<String> names) {
		super(names.get(names.size() - 1));
		
		this.names = Collections.unmodifiableList(names);
	}

	public List<String> getNames() {
		return names;
	}
}
