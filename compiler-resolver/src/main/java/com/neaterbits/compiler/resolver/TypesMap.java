package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.util.ScopedName;

public interface TypesMap<T> {

	T lookupByScopedName(ScopedName scopedName);
}
