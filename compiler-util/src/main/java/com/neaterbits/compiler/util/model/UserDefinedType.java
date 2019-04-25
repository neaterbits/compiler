package com.neaterbits.compiler.util.model;

import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;

public class UserDefinedType {

	private final TypeName typeName;
	private final FileSpec sourceFile;
	private final int parseTreeRef;
	
	public UserDefinedType(TypeName typeName, FileSpec sourceFile, int parseTreeRef) {

		Objects.requireNonNull(typeName);
		Objects.requireNonNull(sourceFile);
		
		this.typeName = typeName;
		this.sourceFile = sourceFile;
		this.parseTreeRef = parseTreeRef;
	}

	public TypeName getTypeName() {
		return typeName;
	}

	public FileSpec getSourceFile() {
		return sourceFile;
	}

	public int getParseTreeRef() {
		return parseTreeRef;
	}
}
