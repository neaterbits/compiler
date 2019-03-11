package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.util.ScopedName;

public interface ResolverLibraryTypes<LIBRARYTYPE> {

	LIBRARYTYPE lookupType(ScopedName scopedName);
	
}
