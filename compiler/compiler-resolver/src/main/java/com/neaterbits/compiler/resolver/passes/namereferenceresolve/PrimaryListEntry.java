package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.resolver.passes.PrimaryType;
import com.neaterbits.compiler.resolver.passes.namereferenceresolve.PrimaryListEntryPart.PartType;

final class PrimaryListEntry {
    
    private final long primaryListReference;
	
	// type of last entry evaluated in primary list
	private final List<PrimaryListEntryPart> parts;

	public PrimaryListEntry(long primaryListReference) {
		this.primaryListReference = primaryListReference;
		this.parts = new ArrayList<>();
	}

	long getPrimaryListReference() {
		return primaryListReference;
	}

	void addType(PartType partType, TypeName typeName, int typeReferenceParseTreeRef) {

	    Objects.requireNonNull(typeName);
		
	    final PrimaryListEntryPart part
	        = new PrimaryListEntryPart(
                    partType,
                    new PrimaryType(typeName, typeReferenceParseTreeRef));
	    
		parts.add(part);
	}

	boolean isEmpty() {
		return parts.isEmpty();
	}
	
	int size() {
	    return parts.size();
	}
	
	PartType getLastPartType() {
	    return parts.get(parts.size() - 1).getPartType();   
	}
	
	TypeName getLastType() {
	    return parts.get(parts.size() - 1).getPrimaryType().getTypeName();
	}

	int getLastTypeParseTreeRef() {
        return parts.get(parts.size() - 1).getPrimaryType().getTypeReferenceParseTreeRef();
    }
}
