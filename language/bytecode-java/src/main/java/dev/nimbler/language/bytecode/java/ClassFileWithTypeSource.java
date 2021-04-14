package dev.nimbler.language.bytecode.java;

import java.util.Objects;

import dev.nimbler.language.bytecode.common.ClassByteCodeWithTypeSource;
import dev.nimbler.language.common.typesources.TypeSource;

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
