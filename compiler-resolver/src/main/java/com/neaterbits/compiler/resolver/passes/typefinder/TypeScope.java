package com.neaterbits.compiler.resolver.passes.typefinder;

import java.util.Objects;

import com.neaterbits.compiler.codemap.TypeVariant;

final class TypeScope extends TypeFinderScope {

    private final TypeVariant typeVariant;
    private final String typeName;
    private final int typeNo;

    TypeScope(TypeVariant typeVariant, String typeName, int typeNo) {

        Objects.requireNonNull(typeVariant);
        Objects.requireNonNull(typeName);

        this.typeVariant = typeVariant;
        this.typeName = typeName;
        this.typeNo = typeNo;
    }

    TypeVariant getTypeVariant() {
        return typeVariant;
    }

    String getTypeName() {
        return typeName;
    }

    int getTypeNo() {
        return typeNo;
    }
}
