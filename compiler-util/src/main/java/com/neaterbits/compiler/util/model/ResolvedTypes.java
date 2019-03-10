package com.neaterbits.compiler.util.model;

import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

public interface ResolvedTypes {

	TypeName lookup(ScopedName scopedName);
	
}
