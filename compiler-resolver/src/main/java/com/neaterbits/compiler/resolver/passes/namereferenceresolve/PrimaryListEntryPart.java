package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.Objects;

import com.neaterbits.compiler.resolver.passes.PrimaryType;

final class PrimaryListEntryPart {

    enum PartType {
        NAME,
        FIELD,
        STATIC_CLASS,
        STATIC_FIELD_ACCESS,
        NAME_ARRAY_ACCESS,
        FIELD_ARRAY_ACCESS
    }
    
    private final PartType partType;
    private final PrimaryType primaryType;
    
    PrimaryListEntryPart(PartType partType, PrimaryType primaryType) {
        
        Objects.requireNonNull(partType);
        Objects.requireNonNull(primaryType);
        
        this.partType = partType;
        this.primaryType = primaryType;
    }

    PartType getPartType() {
        return partType;
    }

    PrimaryType getPrimaryType() {
        return primaryType;
    }
}
