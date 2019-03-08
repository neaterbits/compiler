package com.neaterbits.compiler.common.model;

import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.ast.ScopedName;

public interface ResolvedTypes {

	TypeName lookup(ScopedName scopedName);
	
}
