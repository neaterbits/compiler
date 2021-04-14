package dev.nimbler.compiler.language.java.resolve;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import dev.nimbler.compiler.language.java.JavaUtil;
import dev.nimbler.compiler.model.common.ResolvedTypes;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.TypeSources;

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
	
	public Iterable<TypeName> getTypeNames() {
	    return typeNames;
	}

	@Override
	public boolean hasType(TypeName typeName, TypeSources sources) {

		Objects.requireNonNull(typeName);
		
		return typeNames.contains(typeName);
	}
}
