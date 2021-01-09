package com.neaterbits.compiler.resolver.passes.addfieldspass;

import com.neaterbits.compiler.resolver.passes.TypeScope;
import com.neaterbits.compiler.types.TypeVariant;

final class AddFieldScope extends TypeScope {

    private final int typeNo;

    AddFieldScope(TypeVariant typeVariant, String name, int typeNo) {
        super(typeVariant, name);

        this.typeNo = typeNo;
    }

    int getTypeNo() {
        return typeNo;
    }
}
