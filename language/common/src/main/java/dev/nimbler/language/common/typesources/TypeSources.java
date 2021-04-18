package dev.nimbler.language.common.typesources;

import java.util.Collection;

import org.jutils.EnumMask;

public class TypeSources extends EnumMask<TypeSource> {

	public static final TypeSources ALL = new TypeSources(TypeSource.LIBRARY, TypeSource.COMPILED_PROJECT_MODULE, TypeSource.SOURCE);
	public static final TypeSources LIBRARY = new TypeSources(TypeSource.LIBRARY);
	
	public TypeSources(Collection<TypeSource> values) {
		super(TypeSource.class, values);
	}

	public TypeSources(TypeSource... values) {
		super(TypeSource.class, values);
	}
}
