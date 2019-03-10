package com.neaterbits.compiler.codemap;

public final class DynamicMethodOverrideMap extends MethodOverrideMap {

	@Override
	void addTypeExtendsTypes(int extendingTypeEncoded, int[] extendedTypesEncoded, MethodMap methodMap) {
		throw new UnsupportedOperationException();
	}
}
