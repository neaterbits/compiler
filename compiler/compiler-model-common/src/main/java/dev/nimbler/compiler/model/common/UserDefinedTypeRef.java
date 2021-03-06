package dev.nimbler.compiler.model.common;

import java.util.Objects;

import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.language.common.types.TypeName;

@Deprecated
public class UserDefinedTypeRef extends BaseTypeRef {

	private final FileSpec sourceFile;
	private final int parseTreeRef;

	public UserDefinedTypeRef(TypeName typeName, FileSpec sourceFile, int parseTreeRef) {

		super(typeName);

		Objects.requireNonNull(typeName);
		Objects.requireNonNull(sourceFile);

		if (parseTreeRef < 0) {
			throw new IllegalArgumentException();
		}

		this.sourceFile = sourceFile;
		this.parseTreeRef = parseTreeRef;
	}

	public FileSpec getSourceFile() {
		return sourceFile;
	}

	public int getParseTreeRef() {
		return parseTreeRef;
	}
}
