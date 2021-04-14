package dev.nimbler.compiler.resolver.passes.addfieldspass;

import dev.nimbler.compiler.model.common.TypeScope;
import dev.nimbler.language.common.types.TypeVariant;

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
