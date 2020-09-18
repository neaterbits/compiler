package com.neaterbits.compiler.model.common;

import com.neaterbits.compiler.util.ScopedName;

public interface TypesMap<T> {

	T lookupByScopedName(ScopedName scopedName);
}
