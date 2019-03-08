package com.neaterbits.compiler.common.resolver;

import com.neaterbits.compiler.common.ast.ScopedName;

public interface TypesMap<T> {

	T lookupByScopedName(ScopedName scopedName);
}
