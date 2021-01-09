package com.neaterbits.compiler.model.common;

import java.util.Objects;

import com.neaterbits.compiler.types.TypeVariant;

public class TypeScope extends TypeVisitorScope {

    private final TypeVariant typeVariant;
    private final String name;

    protected TypeScope(TypeVariant typeVariant, String name) {

        Objects.requireNonNull(typeVariant);
        Objects.requireNonNull(name);

        this.typeVariant = typeVariant;
        this.name = name;
    }

    TypeVariant getTypeVariant() {
        return typeVariant;
    }

    String getName() {
        return name;
    }
}
