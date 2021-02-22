package com.neaterbits.compiler.common.loader.ast;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.TypeSpec;
import com.neaterbits.compiler.common.loader.TypeVariant;

public abstract class BaseLoaderType {

	private final FileSpec file;
	private final TypeSpec typeSpec;

	protected BaseLoaderType(FileSpec file, TypeSpec typeSpec) {
		
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
