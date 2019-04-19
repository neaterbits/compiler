package com.neaterbits.compiler.java.resolve;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.java.JavaUtil;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ResolvedTypes;

public class TestResolvedTypes implements ResolvedTypes {

	private final Set<TypeName> typeNames;

	public TestResolvedTypes() {
		this.typeNames = new HashSet<>();
	}
	
	TestResolvedTypes addType(String type) {
		
		final TypeName typeName = JavaUtil.parseToTypeName(type);
		
		typeNames.add(typeName);
		
		return this;
	}

	@Override
	public boolean hasType(TypeName typeName) {

		Objects.requireNonNull(typeName);
		
		return typeNames.contains(typeName);
	}
}
