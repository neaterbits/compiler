package com.neaterbits.compiler.resolver.types;

import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.language.common.types.ScopedName;
import com.neaterbits.language.common.types.TypeVariant;

public abstract class BaseResolverType {

	private final FileSpec file;
	private final TypeSpec typeSpec;

	protected BaseResolverType(FileSpec file, TypeSpec typeSpec) {
		
		Objects.requireNonNull(file);
		Objects.requireNonNull(typeSpec);
		
		this.file = file;
		this.typeSpec = typeSpec;
	}

	public final FileSpec getFile() {
		return file;
	}
	
	public final ScopedName getScopedName() {
		return typeSpec.getScopedName();
	}

	public final boolean isInterface() {
		return typeSpec.isInterface();
	}

	public final boolean isClass() {
		return typeSpec.isClass();
	}

	public final boolean isEnum() {
		return typeSpec.isEnum();
	}

	public final TypeVariant getTypeVariant() {
		return typeSpec.getTypeVariant();
	}
	
	public final TypeSpec getSpec() {
		return typeSpec;
	}

	@Override
	public String toString() {
		return typeSpec.toString();
	}
}
