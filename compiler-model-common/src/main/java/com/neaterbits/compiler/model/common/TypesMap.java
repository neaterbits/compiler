package com.neaterbits.compiler.model.common;

import com.neaterbits.build.types.ScopedName;

public interface TypesMap<T> {

	T lookupByScopedName(ScopedName scopedName);
}
