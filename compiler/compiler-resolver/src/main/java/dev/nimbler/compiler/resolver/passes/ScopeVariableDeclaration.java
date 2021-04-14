package dev.nimbler.compiler.resolver.passes;

import java.util.Objects;

import dev.nimbler.language.common.types.TypeName;

public class ScopeVariableDeclaration {
    
	private final TypeName type;
	private final int typeReferenceParseTreeRef;

	public ScopeVariableDeclaration(TypeName type, int typeReferenceParseTreeRef) {

	    Objects.requireNonNull(type);
	    
		this.type = type;
		this.typeReferenceParseTreeRef = typeReferenceParseTreeRef;
	}

	public TypeName getType() {
		return type;
	}

    public int getTypeReferenceParseTreeRef() {
        return typeReferenceParseTreeRef;
    }
}