package com.neaterbits.compiler.java.bytecode;

import java.util.Objects;

import com.neaterbits.build.types.TypeSource;
import com.neaterbits.compiler.bytecode.common.ClassByteCodeWithTypeSource;

public class ClassFileWithTypeSource extends ClassFile implements ClassByteCodeWithTypeSource {

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
