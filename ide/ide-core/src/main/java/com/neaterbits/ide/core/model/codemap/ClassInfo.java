package com.neaterbits.ide.core.model.codemap;

import java.util.Objects;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.codemap.TypeSuggestion;
import com.neaterbits.language.bytecode.common.ClassBytecode;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.types.TypeVariant;
import com.neaterbits.language.common.typesources.TypeSource;

final class ClassInfo implements TypeSuggestion {

	private final int typeNo;
	private final TypeName typeName;
	private final TypeSource typeSource;
	private final String namespace;
	private final String binaryName;
	private final TypeVariant typeVariant;
	private final SourceFileResourcePath sourceFileResourcePath;

	ClassInfo(
			int typeNo,
			TypeName typeName,
			TypeSource typeSource,
			String namespace,
			String binaryName,
			SourceFileResourcePath sourceFileResourcePath,
			ClassBytecode classByteCode) {
		
		if (typeNo < 0) {
			throw new IllegalArgumentException();
		}
		
		Objects.requireNonNull(typeName);
		Objects.requireNonNull(typeSource);
		
		this.typeNo = typeNo;
		this.typeName = typeName;
		this.typeSource = typeSource;
		this.namespace = namespace;
		this.binaryName = binaryName;
		this.typeVariant = classByteCode.getTypeVariant();
		this.sourceFileResourcePath = sourceFileResourcePath;
	}
	
	int getTypeNo() {
		return typeNo;
	}
	
	TypeSource getTypeSource() {
		return typeSource;
	}

	@Override
	public TypeVariant getType() {
		return typeVariant;
	}

	@Override
	public String getName() {
		return typeName.getName();
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getBinaryName() {
		return binaryName;
	}

	@Override
	public SourceFileResourcePath getSourceFile() {
		return sourceFileResourcePath;
	}
}
