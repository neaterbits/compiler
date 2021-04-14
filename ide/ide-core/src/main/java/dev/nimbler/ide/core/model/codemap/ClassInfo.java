package dev.nimbler.ide.core.model.codemap;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.model.codemap.TypeSuggestion;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypeVariant;
import dev.nimbler.language.common.typesources.TypeSource;

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
