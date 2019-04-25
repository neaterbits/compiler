package com.neaterbits.compiler.java.bytecode;

import java.util.Objects;

import com.neaterbits.compiler.bytecode.common.ClassByteCodeWithTypeSource;
import com.neaterbits.compiler.util.model.TypeSource;

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
