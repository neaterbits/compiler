package com.neaterbits.language.common.types;

public interface TypesMap<T> {

	T lookupByScopedName(ScopedName scopedName);
}
