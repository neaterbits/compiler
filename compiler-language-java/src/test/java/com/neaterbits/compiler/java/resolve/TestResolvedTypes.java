package com.neaterbits.compiler.java.resolve;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ResolvedTypes;

public class TestResolvedTypes implements ResolvedTypes {

	private final Set<TypeName> typeNames;

	public TestResolvedTypes() {
		this.typeNames = new HashSet<>();
	}
	
	TestResolvedTypes addType(String type) {
		
		final String [] parts = Strings.split(type, '.');
		
		final TypeName typeName = new TypeName(
				Arrays.copyOf(parts, parts.length - 1),
				null,
				parts[parts.length - 1]);
		
		typeNames.add(typeName);
		
		return this;
	}

	@Override
	public boolean hasType(TypeName typeName) {

		Objects.requireNonNull(typeName);
		
		return typeNames.contains(typeName);
	}
}
