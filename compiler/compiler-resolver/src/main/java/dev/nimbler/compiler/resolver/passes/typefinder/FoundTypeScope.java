package dev.nimbler.compiler.resolver.passes.typefinder;

import dev.nimbler.compiler.model.common.TypeScope;
import dev.nimbler.language.common.types.TypeVariant;

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
