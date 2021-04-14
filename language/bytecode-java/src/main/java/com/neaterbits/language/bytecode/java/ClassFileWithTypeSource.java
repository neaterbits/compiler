package com.neaterbits.language.bytecode.java;

import java.util.Objects;

import com.neaterbits.language.bytecode.common.ClassByteCodeWithTypeSource;
import com.neaterbits.language.common.typesources.TypeSource;

public final class ClassFileWithTypeSource extends ClassFile implements ClassByteCodeWithTypeSource {

	private final TypeSource typeSource;
	
	public ClassFileWithTypeSource(TypeSource typeSource) {
		
		Objects.requireNonNull(typeSource);
		
		this.typeSource = typeSource;
	}

	@Override
	public TypeSource getTypeSource() {
		return typeSource;
	}
}
