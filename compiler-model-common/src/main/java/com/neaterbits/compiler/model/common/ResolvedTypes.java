package com.neaterbits.compiler.model.common;

import java.util.Arrays;

import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.TypeSources;

@FunctionalInterface
public interface ResolvedTypes {

	default TypeName lookup(ScopedName scopedName, TypeSources sources) {

		TypeName result;
		
		if (scopedName.hasScope()) {
		
			result = null;
			
			final String [] parts = scopedName.getParts();
			
			// Try all combinations of type names
			for (int numOuterTypes = 0; numOuterTypes < parts.length - 1; ++ numOuterTypes) {
				final TypeName typeName = new TypeName(
						numOuterTypes == parts.length - 1
							? null
							: Arrays.copyOf(parts, parts.length - 1 - numOuterTypes),
							
						numOuterTypes == 0
							? null
							: Arrays.copyOfRange(parts, parts.length - 1 - numOuterTypes, parts.length - 1),
						
						parts[parts.length - 1]);
		
				if (hasType(typeName, sources)) {
					result = typeName;
					break;
				}
			}
		}
		else {
			// not fully scoped so cannot resolve
			result = null;
		}
		
		return result;
	}

	boolean hasType(TypeName typeName, TypeSources sources);
	
}
