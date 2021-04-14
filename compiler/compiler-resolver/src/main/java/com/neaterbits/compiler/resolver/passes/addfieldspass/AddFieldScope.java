package com.neaterbits.compiler.resolver.passes.addfieldspass;

import com.neaterbits.compiler.model.common.TypeScope;
import com.neaterbits.language.common.types.TypeVariant;

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
