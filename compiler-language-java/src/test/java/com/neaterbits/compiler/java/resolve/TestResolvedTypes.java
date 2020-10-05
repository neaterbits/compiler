package com.neaterbits.compiler.java.resolve;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.java.JavaUtil;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.util.model.TypeSources;

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
	public boolean hasType(TypeName typeName, TypeSources sources) {

		Objects.requireNonNull(typeName);
		
		return typeNames.contains(typeName);
	}
}
