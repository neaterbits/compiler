package dev.nimbler.ide.core.model.codemap;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.model.codemap.TypeSuggestion;
import dev.nimbler.language.common.types.TypeVariant;

final class TypeSuggestionImpl implements TypeSuggestion {

	private final TypeVariant typeVariant;
	private final String namespace;
	private final String name;
	private final String binaryName;
	private final SourceFileResourcePath sourceFile;

	TypeSuggestionImpl(TypeVariant typeVariant, String namespace, String name, String binaryName, SourceFileResourcePath sourceFile) {
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(name);
		
		this.typeVariant = typeVariant;
		this.name = name;
		this.binaryName = binaryName;
		this.namespace = namespace;
		this.sourceFile = sourceFile;
	}

	@Override
	public TypeVariant getType() {
		return typeVariant;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getBinaryName() {
		return binaryName;
	}

	@Override
	public SourceFileResourcePath getSourceFile() {
		return sourceFile;
	}

	@Override
	public String toString() {
		return "TypeSuggestionImpl [typeVariant=" + typeVariant + ", name=" + name + ", namespace=" + namespace
				+ ", sourceFile=" + sourceFile + "]";
	}
}
