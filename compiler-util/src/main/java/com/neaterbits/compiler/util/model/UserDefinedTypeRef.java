package com.neaterbits.compiler.util.model;

import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;

public class UserDefinedTypeRef extends BaseTypeRef {

	private final FileSpec sourceFile;
	private final int parseTreeRef;
	
	public UserDefinedTypeRef(TypeName typeName, FileSpec sourceFile, int parseTreeRef) {

		super(typeName);
		
		Objects.requireNonNull(typeName);
		Objects.requireNonNull(sourceFile);
		
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
