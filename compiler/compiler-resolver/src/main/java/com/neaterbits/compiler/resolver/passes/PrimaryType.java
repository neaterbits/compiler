package com.neaterbits.compiler.resolver.passes;

import java.util.Objects;

import com.neaterbits.build.types.TypeName;

public class PrimaryType {
    
    private final TypeName typeName;
    private final int typeReferenceParseTreeRef;
    
    public PrimaryType(TypeName typeName, int typeReferenceParseTreeRef) {

        Objects.requireNonNull(typeName);
        
        this.typeName = typeName;
        this.typeReferenceParseTreeRef = typeReferenceParseTreeRef;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public int getTypeReferenceParseTreeRef() {
        return typeReferenceParseTreeRef;
    }
}