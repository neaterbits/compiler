package dev.nimbler.language.common.types;

public interface TypesMap<T> {

	T lookupByScopedName(ScopedName scopedName);
}
