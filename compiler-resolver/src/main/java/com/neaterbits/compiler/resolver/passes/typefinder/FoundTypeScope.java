package com.neaterbits.compiler.resolver.passes.typefinder;

import com.neaterbits.compiler.resolver.passes.TypeScope;
import com.neaterbits.compiler.types.TypeVariant;

final class FoundTypeScope extends TypeScope {

    private final int typeNo;

    FoundTypeScope(TypeVariant typeVariant, String typeName, int typeNo) {
        super(typeVariant, typeName);

        this.typeNo = typeNo;
    }

    int getTypeNo() {
        return typeNo;
    }
}
