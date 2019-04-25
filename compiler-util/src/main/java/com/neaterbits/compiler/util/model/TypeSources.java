package com.neaterbits.compiler.util.model;

import java.util.Collection;

import com.neaterbits.compiler.util.EnumMask;

public class TypeSources extends EnumMask<TypeSource> {

	public static final TypeSources ALL = new TypeSources(TypeSource.LIBRARY, TypeSource.COMPILED_PROJECT_MODULE, TypeSource.SOURCE);
	
	public TypeSources(Collection<TypeSource> values) {
		super(TypeSource.class, values);
	}

	public TypeSources(TypeSource... values) {
		super(TypeSource.class, values);
	}
}
